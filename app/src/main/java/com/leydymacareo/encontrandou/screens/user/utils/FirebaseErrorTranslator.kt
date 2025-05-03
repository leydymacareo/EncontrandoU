package com.leydymacareo.encontrandou.user.utils

import android.content.Context

fun traducirErrorFirebase(context: Context, mensaje: String?): String {
    return when {
        mensaje == null -> "Ocurrió un error desconocido"

        // Registro
        mensaje.contains("email address is already in use", ignoreCase = true) ->
            "Este correo ya está registrado"
        mensaje.contains("The given password is invalid", ignoreCase = true) ->
            "La contraseña no cumple con los requisitos"
        mensaje.contains("badly formatted", ignoreCase = true) ->
            "El correo no tiene un formato válido"

        // Login
        mensaje.contains("There is no user record", ignoreCase = true) ->
            "Este correo no está registrado"
        mensaje.contains("The password is invalid", ignoreCase = true) ->
            "Contraseña incorrecta"
        mensaje.contains("user account has been disabled", ignoreCase = true) ->
            "Tu cuenta ha sido deshabilitada. Contacta a soporte"

        // Otros posibles
        mensaje.contains("network error", ignoreCase = true) ->
            "Error de red. Verifica tu conexión"
        mensaje.contains("too many requests", ignoreCase = true) ->
            "Demasiados intentos fallidos. Intenta más tarde"

        else -> "Error: ${mensaje.trim()}"
    }
}
