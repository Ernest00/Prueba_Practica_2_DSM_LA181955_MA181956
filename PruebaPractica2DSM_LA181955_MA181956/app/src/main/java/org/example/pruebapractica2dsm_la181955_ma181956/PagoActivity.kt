package org.example.pruebapractica2dsm_la181955_ma181956

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.ContentValues.TAG
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
    var queryCarrito: com.google.firebase.database.Query = refCarrito.orderByChild("uid").equalTo(uid)
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

    fun validarCvv(cadena: String) : Boolean {
        val patron = Regex("^[0-9]{3}$")
        return patron.matches(cadena)
    }

    fun validarVencimientoTarjeta(cadena: String) : Boolean {
        val patron = Regex("^[0-9]{2}/[0-9]{4}$")
        return patron.matches(cadena)
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
        val tipoDeDato = object : GenericTypeIndicator<Map<String, String>>() {}

        /*queryCarrito.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Lógica para manejar los datos recibidos
                val mapa = dataSnapshot.getValue(tipoDeDato)
                if (mapa != null) {
                    for ((clave, valor) in mapa) {
                        Log.d(TAG,"$clave: $valor")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejo de errores
            }
        })*/

        btnPago?.setOnClickListener {
            var errores: Int = 0
            if (txtCliente.text.toString().isEmpty()) {
                txtCliente.setError("Ingrese su nombre completo.")
                errores += 1
            }

            if (txtTarjeta.text.toString().isEmpty()) {
                txtTarjeta.setError("Ingrese su número de tarjeta de crédito.")
                errores += 1
            }

            if (txtVencimiento.text.toString().isEmpty()) {
                txtVencimiento.setError("Ingrese la fecha de vencimiento de su tarjeta.")
                errores += 1
            } else if(!validarVencimientoTarjeta(txtVencimiento.text.toString())) {
                txtVencimiento.setError("Digite el mes y año de vencimiento, con el formato (mm/yyyy).")
                errores += 1
            }

            if (txtCvv.text.toString().isEmpty()) {
                txtCvv.setError("Ingrese el CVV de su tarjeta.")
                errores += 1
            } else if(!validarCvv(txtCvv.text.toString())) {
                txtCvv.setError("El número de CVV se compone por un número de 3 dígitos")
                errores += 1
            }

            if (txtDireccion.text.toString().isEmpty()) {
                txtDireccion.setError("Ingrese la dirección de su residencia.")
                errores += 1
            }

            if (errores == 0) {
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

                //Log.d(TAG, "LIstado de productos: ${listaCompra.get(0)}")
                database = FirebaseDatabase.getInstance().getReference("ventas")
                database.child(id).child("uid").setValue(uid)
                database.child(id).child("cliente").setValue(venta.cliente)
                database.child(id).child("tarjeta").setValue(venta.tarjeta)
                database.child(id).child("vencimientotarjeta").setValue(venta.vencimientotarjeta)
                database.child(id).child("cvv").setValue(venta.cvv)
                database.child(id).child("direccion").setValue(venta.direccion)
                database.child(id).child("fecha").setValue(fecha)
                Toast.makeText(this, "Su compra ha sido realizada exitosamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        var refCarrito: DatabaseReference = database.getReference("carrito")
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
    }
}