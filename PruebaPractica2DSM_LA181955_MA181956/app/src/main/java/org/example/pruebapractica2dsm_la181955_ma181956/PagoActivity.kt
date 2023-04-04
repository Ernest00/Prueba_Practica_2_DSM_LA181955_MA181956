package org.example.pruebapractica2dsm_la181955_ma181956

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.ContentValues.TAG
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.example.pruebapractica2dsm_la181955_ma181956.ProductosActivity.Companion.database
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Carrito
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Ordenes
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Venta
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class PagoActivity : AppCompatActivity() {
    var btnPago: Button? = null
    private lateinit var txtCliente : EditText
    private lateinit var txtTarjeta : EditText
    private lateinit var txtVencimiento : EditText
    private lateinit var txtCvv : EditText
    private lateinit var txtDireccion : EditText
    private lateinit var tvTotalPagar : TextView
    private lateinit var database: DatabaseReference
    var consultaCarrito: com.google.firebase.database.Query = refCarrito.child(uid)
    var listaCompra: MutableList<Ordenes>? = null
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
        tvTotalPagar = findViewById<TextView>(R.id.tvTotalVenta)
        listaCompra = ArrayList<Ordenes>()
        var totalPagar: Float = 0F

        // Cargando total de la venta desde actividad anterior
        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            tvTotalPagar.setText("Total a pagar: $" + intent.getStringExtra("totalVenta").toString())
            totalPagar = intent.getStringExtra("totalVenta").toString().toFloat()
        }

        consultaCarrito.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listaCompra!!.removeAll(listaCompra!!)

                for (dato in dataSnapshot.getChildren().toList()) {

                    var precioCantidad = "0"
                    var cantidad = "0"
                    var precio = "0"

                    if (dato.value.toString() != null) {
                        precioCantidad = dato.value.toString()

                        val parts = precioCantidad.split("|") // divide la cadena en dos partes usando el carácter |
                        cantidad = parts[0].toString() // obtiene el primer valor
                        precio = parts[1].toString() // obtiene el segundo valor
                    }
                    val orden: Ordenes? = dataSnapshot.getValue(Ordenes::class.java)

                    orden?.key(uid)
                    orden?.medicamento = dato.key.toString()
                    orden?.precio = precio
                    orden?.cantidad = cantidad

                    if (orden != null) {
                        listaCompra!!.add(orden)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejo de errores
            }
        })

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
                var id = System.currentTimeMillis().toString()
                var fecha: String = obtenerFechaHoraActual()
                var venta = Venta(
                    id,
                    uid,
                    txtCliente.text.toString(),
                    listaCompra as ArrayList<Ordenes>,
                    txtTarjeta.text.toString(),
                    txtVencimiento.text.toString(),
                    txtCvv.text.toString(),
                    txtDireccion.text.toString(),
                    fecha,
                    totalPagar
                )

                // Insertando datos en la tabla ventas de la base de datos
                database = FirebaseDatabase.getInstance().getReference("ventas")
                database.child(venta.id).child("uid").setValue(venta.idcliente)
                database.child(venta.id).child("cliente").setValue(venta.cliente)
                database.child(venta.id).child("tarjeta").setValue(venta.tarjeta)
                database.child(venta.id).child("vencimientotarjeta").setValue(venta.vencimientotarjeta)
                database.child(venta.id).child("cvv").setValue(venta.cvv)
                database.child(venta.id).child("direccion").setValue(venta.direccion)
                database.child(venta.id).child("fecha").setValue(venta.fecha)
                database.child(venta.id).child("total").setValue(venta.total)

                // Insertando datos en la tabla detallesventas
                database = FirebaseDatabase.getInstance().getReference("detallesventas")
                for (dato in venta.medicamentos) {
                    database.child(venta.id).child(dato.medicamento).setValue("${dato.cantidad}|${dato.precio}")
                }
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