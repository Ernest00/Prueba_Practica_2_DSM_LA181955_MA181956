package org.example.pruebapractica2dsm_la181955_ma181956

import android.app.DownloadManager.Query
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Carrito
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Producto

class ProductosActivity : AppCompatActivity() {
    var consultaOrdenada: com.google.firebase.database.Query = refMedicamentos.orderByChild("nombre")
    var medicamentos: MutableList<Producto>? = null
    var listaMedicamentos: ListView? = null
    var btnCarrito : FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
        cargarListaMedicamentos()

        btnCarrito = findViewById<FloatingActionButton>(R.id.btnCarrito)
        btnCarrito?.setOnClickListener {
            val intent = Intent(this, PagoActivity::class.java)
            intent.putExtra("listaMedicamentos", listaCompraMedicamentos)
            startActivity(intent)
        }
    }

    private fun cargarListaMedicamentos() {
        listaMedicamentos = findViewById<ListView>(R.id.ListaMedicamentos)
        medicamentos = ArrayList<Producto>()

        // Cambiarlo refProductos a consultaOrdenada para ordenar lista
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                medicamentos!!.removeAll(medicamentos!!)
                for (dato in dataSnapshot.getChildren()) {
                    val medicamento: Producto? = dato.getValue(Producto::class.java)
                    medicamento?.key(dato.key)
                    if (medicamento != null) {
                        medicamentos!!.add(medicamento)
                    }
                }
                val adapter = ProductoAdapter(
                    this@ProductosActivity,
                    medicamentos as ArrayList<Producto>
                )
                listaMedicamentos!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refMedicamentos: DatabaseReference = database.getReference("medicamentos")
        var refCarrito: DatabaseReference = database.getReference("carrito")
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        var listaCompraMedicamentos = ArrayList<Carrito>()
    }
}