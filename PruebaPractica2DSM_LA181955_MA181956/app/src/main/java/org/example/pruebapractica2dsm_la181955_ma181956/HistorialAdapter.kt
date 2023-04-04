package org.example.pruebapractica2dsm_la181955_ma181956

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.example.pruebapractica2dsm_la181955_ma181956.datos.Venta

class HistorialAdapter(private val context: Activity, var ventas: List<Venta>):
    ArrayAdapter<Venta?>(context, R.layout.historial_layout, ventas) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        // Método invocado tantas veces como elementos tenga la coleccion personas
        // para formar a cada item que se visualizara en la lista personalizada
        val layoutInflater = context.layoutInflater
        var rowview: View? = null
        // optimizando las diversas llamadas que se realizan a este método
        // pues a partir de la segunda llamada el objeto view ya viene formado
        // y no sera necesario hacer el proceso de "inflado" que conlleva tiempo y
        // desgaste de bateria del dispositivo
        rowview = view ?: layoutInflater.inflate(R.layout.historial_layout, null)
        val tvFecha = rowview!!.findViewById<TextView>(R.id.tvFechaVenta)
        val tvTarjeta = rowview.findViewById<TextView>(R.id.tvTarjetaVenta)
        val tvTotal = rowview.findViewById<TextView>(R.id.tvTotalVenta)

        tvFecha.text = "Fecha: " + ventas[position].fecha
        tvTarjeta.text = "N° tarjeta usada: " + ventas[position].tarjeta
        tvTotal.text = "Total: $" + ventas[position].total.toString()

        return rowview
    }
}