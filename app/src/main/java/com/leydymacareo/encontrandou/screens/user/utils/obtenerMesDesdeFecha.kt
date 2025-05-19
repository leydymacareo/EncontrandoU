package com.leydymacareo.encontrandou.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun obtenerMesDesdeFecha(fecha: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.parse(fecha, formatter)
    return date.month.getDisplayName(TextStyle.FULL, Locale("es"))
        .replaceFirstChar { it.uppercase() }
}

fun obtenerAnioDesdeFecha(fecha: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.parse(fecha, formatter)
    return date.year.toString()
}
