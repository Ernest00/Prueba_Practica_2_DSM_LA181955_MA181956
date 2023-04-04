package org.example.pruebapractica2dsm_la181955_ma181956

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.example.pruebapractica2dsm_la181955_ma181956.ProductosActivity.Companion.database
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Carrito
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Venta
import java.util.*
import kotlin.collections.ArrayList

class PagoActivity : AppCompatActivity() {
    var btnPago: Button? = null
    private lateinit var txtCliente : EditText
    private lateinit var txtTarjeta : EditText
    private lateinit var txtVencimiento : EditText
    private lateinit var txtCvv : EditText
    private lateinit var txtDireccion : EditText
    private lateinit var database: DatabaseReference
    var queryCarrito: com.google.firebase.database.Query = ProductosActivity.refCarrito.equalTo(uid)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)
        realizarPago()
    }

    fun obtenerFechaHoraActual() : String {
        val calendar = Calendar.getInstance()
        val fechaActual = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
        val horaActual = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:${calendar.get(Calendar.SECOND)}"
        return "${fechaActual} - ${horaActual}"
    }

    fun realizarPago() {
        btnPago = findViewById<Button>(R.id.btnPagar)
        txtCliente = findViewById<EditText>(R.id.txtNombrePago)
        txtTarjeta = findViewById<EditText>(R.id.txtTarjetaPago)
        txtVencimiento = findViewById<EditText>(R.id.txtVencimientoTarjetaPago)
        txtCvv = findViewById<EditText>(R.id.txtCvvTarjetaPago)
        txtDireccion = findViewById<EditText>(R.id.txtDireccionPago)

        var id = System.currentTimeMillis().toString()
        var fecha: String = obtenerFechaHoraActual()
        var listaCompra: ArrayList<Carrito> = ArrayList<Carrito>()

        queryCarrito.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaCompra!!.removeAll(listaCompra!!)
                for (dato in snapshot.getChildren()){
                    val compra: Carrito? = dato.getValue(Carrito::class.java)
                    compra?.key(dato.key)
                    if(compra != null) {
                        listaCompra!!.add(compra)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        btnPago?.setOnClickListener {
            var venta = Venta(
                id,
                uid,
                txtCliente.text.toString(),
                listaCompra,
                txtTarjeta.text.toString(),
                txtVencimiento.text.toString(),
                txtCvv.text.toString(),
                txtDireccion.text.toString(),
                fecha
            )

            database = FirebaseDatabase.getInstance().getReference("ventas")
            database.child(id).child("uid").setValue(uid)
            database.child(id).child("cliente").setValue(venta.cliente)
            database.child(id).child("tarjeta").setValue(venta.tarjeta)
            database.child(id).child("vencimientotarjeta").setValue(venta.vencimientotarjeta)
            database.child(id).child("cvv").setValue(venta.cvv)
            database.child(id).child("direccion").setValue(venta.direccion)
            database.child(id).child("fecha").setValue(fecha)

        }
    }

    companion object {
        var refCarrito: DatabaseReference = database.getReference("carrito")
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
    }
}