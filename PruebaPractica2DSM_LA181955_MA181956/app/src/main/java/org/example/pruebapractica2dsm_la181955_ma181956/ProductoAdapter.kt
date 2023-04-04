package org.example.pruebapractica2dsm_la181955_ma181956
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Producto
import org.w3c.dom.Text

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

        tvNombre.text = "Nombre : " + productos[position].indicaciones
        tvPrecio.text = "Precio : $" + productos[position].precio
        tvIndicaciones.text = "Indicaciones: " + productos[position].indicaciones
        tvContraIndicaciones.text = "Contra-indicaciones: " + productos[position].contraindicaciones
        return rowview
    }
}