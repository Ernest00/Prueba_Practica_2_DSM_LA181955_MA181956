package org.example.pruebapractica2dsm_la181955_ma181956
import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Carrito
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Producto

class ProductoAdapter(private val context: Activity, var productos: List<Producto>) :
    ArrayAdapter<Producto?>(context, R.layout.producto_layout, productos) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        // Método invocado tantas veces como elementos tenga la coleccion personas
        // para formar a cada item que se visualizara en la lista personalizada
        val layoutInflater = context.layoutInflater
        var rowview: View? = null
        // optimizando las diversas llamadas que se realizan a este método
        // pues a partir de la segunda llamada el objeto view ya viene formado
        // y no sera necesario hacer el proceso de "inflado" que conlleva tiempo y
        // desgaste de bateria del dispositivo
        rowview = view ?: layoutInflater.inflate(R.layout.producto_layout, null)
        val tvNombre = rowview!!.findViewById<TextView>(R.id.tvNombre)
        val tvPrecio = rowview.findViewById<TextView>(R.id.tvPrecio)
        val tvIndicaciones = rowview.findViewById<TextView>(R.id.tvIndicaciones)
        val tvContraIndicaciones = rowview.findViewById<TextView>(R.id.tvContraIndicaciones)
        val ivImagen = rowview.findViewById<ImageView>(R.id.imagenProducto)
        val btnAnadir = rowview.findViewById<Button>(R.id.btnAnadir)
        val tvCantidad = rowview.findViewById<EditText>(R.id.txtCantidad)

        tvNombre.text = "Nombre: " + productos[position].nombre
        tvPrecio.text = "Precio: $" + productos[position].precio.toString()
        tvIndicaciones.text = "Indicaciones: " + productos[position].indicaciones
        tvContraIndicaciones.text = "Contra-indicaciones: " + productos[position].contraindicaciones
        Picasso.get().load(productos[position].imagen).into(ivImagen)

        btnAnadir.setOnClickListener {
            var uid: String = ProductosActivity.uid
            var medicamento : String = tvNombre.text.toString().replace("Nombre: ", "")
            var cantidad: Int = tvCantidad.text.toString().toInt()
            var precio: Float = tvPrecio.text.toString().replace("Precio: $", "" ).toFloat()
            var compra = Carrito(uid, medicamento, cantidad, precio)
            tvCantidad.setText("")
            ProductosActivity.listaCompraMedicamentos.add(compra)
            ProductosActivity.refCarrito.child(compra.uid).child(compra.medicamento).setValue("${compra.cantidad}|${compra.precio}")
        }

        return rowview
    }
}