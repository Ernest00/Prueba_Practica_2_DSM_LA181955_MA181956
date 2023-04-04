package org.example.pruebapractica2dsm_la181955_ma181956.datos

class Venta {
    fun key(key: String?) {

    }

    var id: String = ""
    var idcliente: String = ""
    var cliente: String = ""
    var tarjeta: String = ""
    var vencimientotarjeta: String = ""
    var cvv: String = ""
    var direccion: String = ""
    var fecha: String = ""
    var total: Float = 0F
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}

    constructor(
        id: String,
        idcliente: String,
        cliente: String,
        tarjeta: String,
        vencimientotarjeta: String,
        cvv: String,
        direccion: String,
        fecha: String,
        total: Float
    ) {
        this.id = id
        this.idcliente = idcliente
        this.cliente = cliente
        this.tarjeta = tarjeta
        this.vencimientotarjeta = vencimientotarjeta
        this.cvv = cvv
        this.direccion = direccion
        this.fecha = fecha
        this.total = total
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "idcliente" to idcliente,
            "cliente" to cliente,
            "tarjeta" to tarjeta,
            "vencimientotarjeta" to vencimientotarjeta,
            "cvv" to cvv,
            "direccion" to direccion,
            "fecha" to fecha,
            "total" to total,
            "key" to key,
            "per" to per
        )
    }
}