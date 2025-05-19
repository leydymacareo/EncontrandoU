package com.leydymacareo.encontrandou.models

data class SolicitudFormState(
    var nombreObjeto: String = "",
    var lugar: String = "",
    var fechaAproximada: String = "",
    var horaAproximada: String = "",
    var categoria: String = "",
    var color: String = "",
    var marca: String = "",
    var descripcion: String = ""
)
