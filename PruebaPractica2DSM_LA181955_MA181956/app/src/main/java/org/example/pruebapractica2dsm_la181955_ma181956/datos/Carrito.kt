package org.example.pruebapractica2dsm_la181955_ma181956.datos

class Carrito {
    fun key(key: String?) {

    }

    var uid: String = ""
    var medicamento: String = ""
    var precio: Float = 0F
    var cantidad: Int = 0
    var key: String = ""
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}

    constructor(uid: String, medicamento: String, cantidad: Int, precio: Float) {
        this.uid = uid
        this.medicamento = medicamento
        this.cantidad = cantidad
        this.precio = precio
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "medicamento" to medicamento,
            "precio" to precio,
            "cantidad" to cantidad,
            "key" to key,
            "per" to per
        )
    }
}