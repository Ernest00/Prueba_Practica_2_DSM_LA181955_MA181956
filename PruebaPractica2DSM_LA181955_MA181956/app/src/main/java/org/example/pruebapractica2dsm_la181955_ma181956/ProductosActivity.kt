package org.example.pruebapractica2dsm_la181955_ma181956

import android.app.DownloadManager.Query
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ProductosActivity : AppCompatActivity() {
    var consultaOrdenada: Query =

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
    }
}