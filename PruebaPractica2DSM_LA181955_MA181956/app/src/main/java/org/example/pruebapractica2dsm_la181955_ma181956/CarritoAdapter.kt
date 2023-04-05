package org.example.pruebapractica2dsm_la181955_ma181956

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Ordenes

class CarritoAdapter(private val context: Activity, var ordenes: List<Ordenes>) :
    ArrayAdapter<Ordenes?>(context, R.layout.ordencompra_layout, ordenes) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        // Método invocado tantas veces como elementos tenga la coleccion personas
        // para formar a cada item que se visualizara en la lista personalizada
        val layoutInflater = context.layoutInflater
        var rowview: View? = null
        // optimizando las diversas llamadas que se realizan a este método
        // pues a partir de la segunda llamada el objeto view ya viene formado
        // y no sera necesario hacer el proceso de "inflado" que conlleva tiempo y
        // desgaste de bateria del dispositivo
        rowview = view ?: layoutInflater.inflate(R.layout.ordencompra_layout, null)
        val tvNombre = rowview!!.findViewById<TextView>(R.id.tvNombre)
        val tvPrecio = rowview.findViewById<TextView>(R.id.tvPrecio)
        val tvCantidad = rowview.findViewById<TextView>(R.id.tvCantidad)
        tvNombre.text = "${context.getString(R.string.producto_nombre)}: " + ordenes[position].medicamento

        tvPrecio.text = "${context.getString(R.string.producto_precio)}: $" + ordenes[position].precio
        tvCantidad.text = "${context.getString(R.string.producto_cantidad)}: " + ordenes[position].cantidad
        return rowview
    }
}