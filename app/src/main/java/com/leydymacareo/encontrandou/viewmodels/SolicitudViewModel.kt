package com.leydymacareo.encontrandou.viewmodels

import androidx.lifecycle.ViewModel
import com.leydymacareo.encontrandou.models.Solicitud
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class SolicitudViewModel : ViewModel() {

    private val _solicitudes = MutableStateFlow<List<Solicitud>>(emptyList())
    val solicitudes: StateFlow<List<Solicitud>> = _solicitudes

    fun agregarSolicitud(solicitud: Solicitud) {
        _solicitudes.value = _solicitudes.value + solicitud
    }

    fun obtenerPorId(id: String): Solicitud? {
        return _solicitudes.value.find { it.id == id }
    }

    fun generarIdUnico(): String = UUID.randomUUID().toString()
}