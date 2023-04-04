package org.example.pruebapractica2dsm_la181955_ma181956

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Producto
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Venta

class HistorialActivity : AppCompatActivity() {
    var consultaVentas: com.google.firebase.database.Query = refVentas.orderByChild("uid")
    var historialCompras: MutableList<Venta>? = null
    var listaHistorial: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)
    }

    private fun cargarHistorialCompras() {
        listaHistorial = findViewById<ListView>(R.id.historialCompras)
        historialCompras = ArrayList<Venta>()

        consultaVentas.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                historialCompras!!.removeAll(historialCompras!!)
                for (dato in dataSnapshot.getChildren()) {
                    val venta: Venta? = dato.getValue(Venta::class.java)
                    venta?.key(dato.key)
                    if (venta != null) {
                        historialCompras!!.add(venta)
                    }
                }
                val adapter = HistorialAdapter(
                    this@HistorialActivity,
                    historialCompras as ArrayList<Venta>
                )
                listaHistorial!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refVentas: DatabaseReference = database.getReference("ventas")
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
    }
}