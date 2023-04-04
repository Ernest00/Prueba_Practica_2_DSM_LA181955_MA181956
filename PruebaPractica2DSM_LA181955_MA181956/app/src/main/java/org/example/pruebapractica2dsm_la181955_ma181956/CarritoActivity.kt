package org.example.pruebapractica2dsm_la181955_ma181956

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Carrito
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Ordenes
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Producto

class CarritoActivity : AppCompatActivity() {
    val user = FirebaseAuth.getInstance().currentUser
    val uid = user?.uid.toString()
    var consultaOrdenada: com.google.firebase.database.Query = refCarrito
    var consultaMedicamentos: com.google.firebase.database.Query = refMedicamentos
    var ordenes: MutableList<Ordenes>? = null
    var listaCarrito: ListView? = null
    var medicamentos: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        cargarListaProductos()
        cargarListaCarrito()

        Log.d(TAG, "EL UID ES: ${uid}")
    }

    private fun cargarListaProductos() {
        medicamentos = ArrayList<String>()
        consultaMedicamentos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                medicamentos!!.removeAll(medicamentos!!)
                for (data in snapshot.getChildren()) {
                    //Log.d(TAG, "MEDICAMENTO " + data.toString())
                    // Log.d(TAG, "MEDICAMENTO2 " + data.child("nombre").getValue().toString())
                    // var sm :String = snapshot.child("nombre").getValue().toString()
                    (medicamentos as ArrayList<String>).add(
                        data.child("nombre").getValue().toString()
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    TAG,
                    "Error al obtener los valores del objeto",
                    databaseError.toException()
                )
            }
        })
    }

    private fun cargarListaCarrito() {
        listaCarrito = findViewById<ListView>(R.id.ListaCarrito)
        ordenes = ArrayList<Ordenes>()


        // Cambiarlo refProductos a consultaOrdenada para ordenar lista
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                ordenes!!.removeAll(ordenes!!)
                for (dato in dataSnapshot.getChildren()) {
                    Log.d(TAG, "UID- ${uid} - DATTA? " + dato.toString())
                    Log.d(TAG, "Key1- " + dato.key.toString())
                    Log.d(TAG, "Value1- " + dato.value.toString())
                    for (item in medicamentos!!) {
                        var precioCantidad =
                            dataSnapshot.child(uid).child(item).getValue().toString()
                        Log.d(
                            TAG,
                            "Valor de ${item}- " + precioCantidad
                        )

                        val orden: Ordenes? = dato.getValue(Ordenes::class.java)
                        orden?.key(uid)

                        var cantidad =""
                        var precio=""
                        if(precioCantidad!=null){
                            val parts =
                                precioCantidad.split("|") // divide la cadena en dos partes usando el carácter |
                            Log.d(TAG, "MX - " + parts[0].toString())

                            cantidad= parts[0].toString() // obtiene el primer valor
                            Log.d(TAG, "MX1 - " + parts[1].toString())
                            precio = parts[1].toString() // obtiene el segundo valor
                        }

                        orden?.medicamento = item
                        orden?.precio = precio.toString()
                        orden?.cantidad = cantidad.toString()

                        Log.d(
                            TAG,
                            "Valores ${orden?.medicamento}- " + precioCantidad
                        )

                        if (orden != null) {
                            ordenes!!.add(orden)
                        }


                    }

                }
                val adapter = CarritoAdapter(
                    this@CarritoActivity,
                    ordenes as ArrayList<Ordenes>
                )
                listaCarrito!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    TAG,
                    "Error al obtener los valores del objeto $uid",
                    databaseError.toException()
                )
            }
        })
    }

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refCarrito: DatabaseReference = database.getReference().child("carrito")
        var refMedicamentos: DatabaseReference = database.getReference().child("medicamentos")

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        var refOrdenes = refCarrito.child(uid)
    }
}