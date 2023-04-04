package org.example.pruebapractica2dsm_la181955_ma181956.datos

class Producto {
    fun key(key: String?) {

    }

    var nombre: String? = null
    var precio: String? = null
    var indicaciones: String? = null
    var contraindicaciones: String? = null
    var imagen: String? = null
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nombre" to nombre,
            "precio" to precio,
            "indicaciones" to indicaciones,
            "contraindicaciones" to contraindicaciones,
            "imagen" to imagen,
            "key" to key,
            "per" to per
        )
    }
}