package org.example.pruebapractica2dsm_la181955_ma181956

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Carrito
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Ordenes
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Producto

class CarritoActivity : AppCompatActivity() {
    val user = FirebaseAuth.getInstance().currentUser
    val uid = user?.uid.toString()
    var consultaOrdenada: com.google.firebase.database.Query = refCarrito.child(uid)
    var consultaMedicamentos: com.google.firebase.database.Query = refMedicamentos
    var ordenes: MutableList<Ordenes>? = null
    var listaCarrito: ListView? = null
    private lateinit var btnPasoPago: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        cargarListaCarrito()

        btnPasoPago = findViewById<Button>(R.id.btnPasoPago)
        btnPasoPago.setOnClickListener {
            val intent = Intent(this, PagoActivity::class.java)
            startActivity(intent)
        }

    }


    private fun cargarListaCarrito() {
        listaCarrito = findViewById<ListView>(R.id.ListaCarrito)
        ordenes = ArrayList<Ordenes>()

        // Cambiarlo refProductos a consultaOrdenada para ordenar lista
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                ordenes!!.removeAll(ordenes!!)
                Log.d(
                    TAG,
                    "Consultando la bdd se obtuvo\n-> ${
                        dataSnapshot.getChildren().toList().toString()
                    }"
                )

                for (dato in dataSnapshot.getChildren().toList()) {

                    var precioCantidad = "0"
                    var cantidad = "0"
                    var precio = "0"

                    if (dato.value.toString() != null) {
                        precioCantidad = dato.value.toString()

                        val parts =
                            precioCantidad.split("|") // divide la cadena en dos partes usando el carácter |
                        cantidad = parts[0].toString() // obtiene el primer valor
                        precio = parts[1].toString() // obtiene el segundo valor
                    }
                    val orden: Ordenes? = dataSnapshot.getValue(Ordenes::class.java)


                    orden?.key(uid)
                    orden?.medicamento = dato.key.toString()
                    orden?.precio = precio
                    orden?.cantidad = cantidad

                    if (orden != null) {
                        ordenes!!.add(orden)
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