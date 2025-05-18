package com.leydymacareo.encontrandou.models

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
    val estado: String = "",
    val imagenUri: String? = null,
    val sessionId: String = ""
)
