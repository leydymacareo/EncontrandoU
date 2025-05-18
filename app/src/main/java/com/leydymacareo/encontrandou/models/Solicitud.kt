package com.leydymacareo.encontrandou.models

import EstadoSolicitud

data class Solicitud(
    var key: String = "",
    val id: String = "",
    val nombreObjeto: String = "",
    val propietario: String = "",
    val fecha: String = "",
    val hora: String = "",
    val categoria: String = "",
    val color: String = "",
    val lugar: String = "",
    val descripcion: String = "",
    var estado: EstadoSolicitud = EstadoSolicitud.PENDIENTE,
    val imagenUri: String? = null,
    val sessionId: String = "",
    var objetoId: String? = null,
    var codigoObjetoAsignado: String? = null
)
