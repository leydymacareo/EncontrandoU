package com.leydymacareo.encontrandou.models

data class ObjetoEncontrado(
    var key: String = "",
    val id: String = "",
    val nombre: String = "",
    val fecha: String = "",
    val estado: String = "Disponible", // Estado inicial
    val imagenUri: String? = null,
    val descripcion: String = "",
    val categoria: String = "",
    val color: String = "",
    val marca: String? = null,
    val lugar: String = "",
    val fechaAproximada: String = "",
    val horaAproximada: String = ""

)

