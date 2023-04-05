package org.example.pruebapractica2dsm_la181955_ma181956

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.example.pruebapractica2dsm_la181955_ma181956.ProductosActivity.Companion.database
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Ordenes
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Venta
import java.util.*
import kotlin.collections.ArrayList

class PagoActivity : AppCompatActivity() {
    var btnPago: Button? = null
    private lateinit var txtCliente: EditText
    private lateinit var txtTarjeta: EditText
    private lateinit var txtVencimiento: EditText
    private lateinit var txtCvv: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var tvTotalPagar: TextView
    private lateinit var database: DatabaseReference
    var consultaCarrito: com.google.firebase.database.Query = refCarrito.child(uid)
    var listaCompra: MutableList<Ordenes>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)
        realizarPago()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> {
                FirebaseAuth.getInstance().signOut().also {
                    Toast.makeText(this, resources.getString(R.string.cerrarSesion), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.action_productos -> {
                val intent = Intent(this, ProductosActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_carrito -> {
                val intent = Intent(this, CarritoActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_history -> {
                val intent = Intent(this, HistorialActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun obtenerFechaHoraActual(): String {
        val calendar = Calendar.getInstance()
        val fechaActual = "${calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')}/${
            (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        }/${calendar.get(Calendar.YEAR)}"
        val horaActual = "${calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')}:${
            calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
        }:${calendar.get(Calendar.SECOND).toString().padStart(2, '0')}"
        return "$fechaActual - $horaActual"
    }

    fun validarNumeroTarjeta(tarjeta: String) : Boolean {
        val patron = Regex("^[0-9]{15,16}|(([0-9]{4}\\s){3}[0-9]{3,4})$")
        return patron.matches(tarjeta)
    }

    fun validarCvv(cadena: String): Boolean {
        val patron = Regex("^[0-9]{3}$")
        return patron.matches(cadena)
    }

    fun validarVencimientoTarjeta(cadena: String): Boolean {
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
            tvTotalPagar.setText(
                "${resources.getString(R.string.total_a_pagar)}: $" + intent.getStringExtra("totalVenta").toString()
            )
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
                txtCliente.setError(resources.getString(R.string.error_nombre))
                errores += 1
            }

            if (txtTarjeta.text.toString().isEmpty()) {
                txtTarjeta.setError(resources.getString(R.string.error_tarjeta))
                errores += 1
            } else if (!validarNumeroTarjeta(txtTarjeta.text.toString())) {
                txtTarjeta.setError(resources.getString(R.string.error_num_tarjeta))
                errores += 1
            }

            if (txtVencimiento.text.toString().isEmpty()) {
                txtVencimiento.setError(resources.getString(R.string.error_fechaTarjeta))
                errores += 1
            } else if (!validarVencimientoTarjeta(txtVencimiento.text.toString())) {
                txtVencimiento.setError(resources.getString(R.string.error_digitarFecha))
                errores += 1
            }

            if (txtCvv.text.toString().isEmpty()) {
                txtCvv.setError(resources.getString(R.string.error_cvv))
                errores += 1
            } else if (!validarCvv(txtCvv.text.toString())) {
                txtCvv.setError(resources.getString(R.string.error_digitarCvv))
                errores += 1
            }

            if (txtDireccion.text.toString().isEmpty()) {
                txtDireccion.setError(resources.getString(R.string.error_direccion))
                errores += 1
            }

            if (errores == 0) {
                var id = System.currentTimeMillis().toString()
                var fecha: String = obtenerFechaHoraActual()
                var venta = Venta(
                    id,
                    uid,
                    txtCliente.text.toString(),
                    txtTarjeta.text.toString(),
                    txtVencimiento.text.toString(),
                    txtCvv.text.toString(),
                    txtDireccion.text.toString(),
                    fecha,
                    totalPagar
                )

                // Insertando datos en la tabla ventas de la base de datos
                database = FirebaseDatabase.getInstance().getReference("ventas")
                database.child(venta.id).child("id").setValue(venta.id)
                database.child(venta.id).child("idcliente").setValue(venta.idcliente)
                database.child(venta.id).child("cliente").setValue(venta.cliente)
                database.child(venta.id).child("tarjeta").setValue(venta.tarjeta)
                database.child(venta.id).child("vencimientotarjeta")
                    .setValue(venta.vencimientotarjeta)
                database.child(venta.id).child("cvv").setValue(venta.cvv)
                database.child(venta.id).child("direccion").setValue(venta.direccion)
                database.child(venta.id).child("fecha").setValue(venta.fecha)
                database.child(venta.id).child("total").setValue(venta.total)

                // Insertando datos en la tabla detallesventas
                database = FirebaseDatabase.getInstance().getReference("detallesventas")
                for (dato in listaCompra as ArrayList<Ordenes>) {
                    database.child(venta.id).child(dato.medicamento)
                        .setValue("${dato.cantidad}|${dato.precio}")
                }
                Toast.makeText(this, resources.getString(R.string.compra_exitosa), Toast.LENGTH_SHORT)
                    .show()

                //Se limpia el carrito una vez realizada la compra
                refCarritoEliminar.removeValue()
                // Se muestra la actividad del historial de compras
                val intent = Intent(this, HistorialActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }

    companion object {
        var refCarrito: DatabaseReference = database.getReference("carrito")

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        var refCarritoEliminar: DatabaseReference = database.getReference("carrito/${uid}")


    }
}