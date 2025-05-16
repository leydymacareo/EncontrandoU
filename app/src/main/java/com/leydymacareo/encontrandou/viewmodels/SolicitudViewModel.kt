package com.leydymacareo.encontrandou.viewmodels

import androidx.lifecycle.ViewModel
import com.leydymacareo.encontrandou.models.ObjetoEncontrado
import com.leydymacareo.encontrandou.models.Solicitud
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class SolicitudViewModel : ViewModel() {

    private val _solicitudes = MutableStateFlow<List<Solicitud>>(emptyList())
    val solicitudes: StateFlow<List<Solicitud>> = _solicitudes

    private val _objetosEncontrados = MutableStateFlow<List<ObjetoEncontrado>>(emptyList())
    val objetosEncontrados: StateFlow<List<ObjetoEncontrado>> = _objetosEncontrados

    private var contadorSolicitudes = 0
    private var contadorObjetos = 0

    fun agregarSolicitud(solicitud: Solicitud) {
        _solicitudes.value = _solicitudes.value + solicitud
    }

    fun agregarObjeto(objeto: ObjetoEncontrado) {
        _objetosEncontrados.value = _objetosEncontrados.value + objeto
    }

    fun obtenerPorId(id: String): Solicitud? {
        return _solicitudes.value.find { it.id == id }
    }

    fun generarCodigoSolicitud(): String {
        contadorSolicitudes += 1
        return "SOL-${contadorSolicitudes.toString().padStart(4, '0')}"
    }

    fun generarCodigoObjeto(): String {
        contadorObjetos += 1
        val fecha = obtenerFechaCorta() // formato: 160525
        return "OBJ-$fecha-${contadorObjetos.toString().padStart(4, '0')}"
    }

    private fun obtenerFechaCorta(): String {
        val formatter = SimpleDateFormat("ddMMyy", Locale.getDefault())
        return formatter.format(Date())
    }
}
