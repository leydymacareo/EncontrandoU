package com.leydymacareo.encontrandou.models

import EstadoObjeto

data class ObjetoEncontrado(
    var key: String = "",
    val id: String = "",
    val nombre: String = "",
    val fecha: String = "",
    var estado: EstadoObjeto = EstadoObjeto.DISPONIBLE,
    val imagenUri: String? = null,
    val descripcion: String = "",
    val categoria: String = "",
    val color: String = "",
    val marca: String? = null,
    val lugar: String = "",
    val fechaAproximada: String = "",
    val horaAproximada: String = "",
    var solicitudAsignadaId: String? = null,
    var codigoSolicitudAsignada: String? = null

)

