package org.example.pruebapractica2dsm_la181955_ma181956.datos

class Ordenes {

    fun key(key: String?) {

    }

    var medicamento : String =""
    var precio: String = ""
    var cantidad: String = ""
    var uid: String = ""
    var key: String = ""
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}

    constructor(uid: String, medicamento:String, precio: String, cantidad: String) {
        this.uid = uid
        this.medicamento = medicamento
        this.precio = precio
        this.cantidad = cantidad
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