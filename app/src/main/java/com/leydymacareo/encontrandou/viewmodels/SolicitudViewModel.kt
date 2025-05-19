package com.leydymacareo.encontrandou.viewmodels

import EstadoObjeto
import EstadoSolicitud
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
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

    private val db = FirebaseFirestore.getInstance()

    init {
        Log.d("SolicitudViewModel", "SolicitudViewModel iniciado")
    }

    fun agregarSolicitud(solicitud: Solicitud) {
        _solicitudes.value = _solicitudes.value + solicitud
        db.collection("solicitudes")
            .add(solicitud)
            .addOnSuccessListener { docRef ->
                Log.d("SolicitudViewModel", "Solicitud guardada: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al guardar solicitud", e)
            }
    }

    fun agregarObjeto(objeto: ObjetoEncontrado) {
        _objetosEncontrados.value = _objetosEncontrados.value + objeto
        db.collection("objetos")
            .add(objeto)
            .addOnSuccessListener { docRef ->
                Log.d("SolicitudViewModel", "Objeto guardado: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al guardar objeto", e)
            }
    }

    fun obtenerSolicitudPorId(key: String): Solicitud? {
        Log.d("SolicitudViewModel", "obtenerSolicitudPorId: $key");
        return _solicitudes.value.find { it.key == key }
    }

    fun obtenerObjectoPorId(key: String): ObjetoEncontrado? {
        Log.d("SolicitudViewModel", "obtenerObjectoPorId: $key");
        return _objetosEncontrados.value.find { it.key == key }
    }

    fun generarCodigoSolicitud(sessionId: String): String {
        contadorSolicitudes += 1
        val fecha = obtenerFechaCorta()
        return "SOL-$fecha-${sessionId}-${contadorSolicitudes.toString().padStart(4, '0')}"
    }

    fun generarCodigoObjeto(sessionId: String): String {
        contadorObjetos += 1
        val fecha = obtenerFechaCorta()
        return "OBJ-$fecha-${sessionId}-${contadorObjetos.toString().padStart(4, '0')}"
    }

    private fun obtenerFechaCorta(): String {
        val formatter = SimpleDateFormat("ddMMyy", Locale.getDefault())
        return formatter.format(Date())
    }

    fun cargarSolicitudesDesdeFirestore() {
        db.collection("solicitudes")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    try {
                        val estado = safeEstadoSolicitud(doc.getString("estado") ?: "")
                        Solicitud(
                            key = doc.id,
                            id = doc.getString("id") ?: "",
                            nombreObjeto = doc.getString("nombreObjeto") ?: "",
                            propietario = doc.getString("propietario") ?: "",
                            fecha = doc.getString("fecha") ?: "",
                            hora = doc.getString("hora") ?: "",
                            categoria = doc.getString("categoria") ?: "",
                            color = doc.getString("color") ?: "",
                            lugar = doc.getString("lugar") ?: "",
                            descripcion = doc.getString("descripcion") ?: "",
                            estado = estado,
                            imagenUri = doc.getString("imagenUri"),
                            sessionId = doc.getString("sessionId") ?: "",
                            objetoId = doc.getString("objetoId"),
                            codigoObjetoAsignado = doc.getString("codigoObjetoAsignado")
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                _solicitudes.value = lista
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al cargar solicitudes", e)
            }
    }

    fun cargarObjetosDesdeFirestore() {
        db.collection("objetos")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    try {
                        val estado = safeEstadoObjeto(doc.getString("estado") ?: "")
                        ObjetoEncontrado(
                            key = doc.id,
                            id = doc.getString("id") ?: "",
                            nombre = doc.getString("nombre") ?: "",
                            fecha = doc.getString("fecha") ?: "",
                            estado = estado,
                            imagenUri = doc.getString("imagenUri"),
                            descripcion = doc.getString("descripcion") ?: "",
                            categoria = doc.getString("categoria") ?: "",
                            color = doc.getString("color") ?: "",
                            marca = doc.getString("marca"),
                            lugar = doc.getString("lugar") ?: "",
                            fechaAproximada = doc.getString("fechaAproximada") ?: "",
                            horaAproximada = doc.getString("horaAproximada") ?: "",
                            solicitudAsignadaId = doc.getString("solicitudAsignadaId"),
                            codigoSolicitudAsignada = doc.getString("codigoSolicitudAsignada")
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                _objetosEncontrados.value = lista
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al cargar objetos", e)
            }
    }

    fun cargarObjetoDesdeFirestore(id: String) {
        db.collection("objetos").document(id)
            .get()
            .addOnSuccessListener { result ->
                try {
                    val estado = safeEstadoObjeto(result.getString("estado") ?: "")
                    ObjetoEncontrado(
                        key = result.id,
                        id = result.getString("id") ?: "",
                        nombre = result.getString("nombre") ?: "",
                        fecha = result.getString("fecha") ?: "",
                        estado = estado,
                        imagenUri = result.getString("imagenUri"),
                        descripcion = result.getString("descripcion") ?: "",
                        categoria = result.getString("categoria") ?: "",
                        color = result.getString("color") ?: "",
                        marca = result.getString("marca"),
                        lugar = result.getString("lugar") ?: "",
                        fechaAproximada = result.getString("fechaAproximada") ?: "",
                        horaAproximada = result.getString("horaAproximada") ?: "",
                        solicitudAsignadaId = result.getString("solicitudAsignadaId"),
                        codigoSolicitudAsignada = result.getString("codigoSolicitudAsignada")
                    )
                } catch (e: Exception) {
                    null
                }

            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al cargar objetos", e)
            }
    }

    fun cargarSolicitudesDeUsuario(email: String) {
        db.collection("solicitudes")
            .whereEqualTo("propietario", email)
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    try {
                        val estado = safeEstadoSolicitud(doc.getString("estado") ?: "")
                        Solicitud(
                            key = doc.id,
                            id = doc.getString("id") ?: "",
                            nombreObjeto = doc.getString("nombreObjeto") ?: "",
                            propietario = doc.getString("propietario") ?: "",
                            fecha = doc.getString("fecha") ?: "",
                            hora = doc.getString("hora") ?: "",
                            categoria = doc.getString("categoria") ?: "",
                            color = doc.getString("color") ?: "",
                            lugar = doc.getString("lugar") ?: "",
                            descripcion = doc.getString("descripcion") ?: "",
                            estado = estado,
                            imagenUri = doc.getString("imagenUri"),
                            sessionId = doc.getString("sessionId") ?: "",
                            objetoId = doc.getString("objetoId"),
                            codigoObjetoAsignado = doc.getString("codigoObjetoAsignado")
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                _solicitudes.value = lista
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al cargar solicitudes del usuario", e)
            }
    }

    // ✅ FUNCIONES DE ENUM SEGURO
    private fun safeEstadoSolicitud(value: String): EstadoSolicitud =
        try {
            EstadoSolicitud.valueOf(value)
        } catch (e: Exception) {
            EstadoSolicitud.PENDIENTE
        }

    private fun safeEstadoObjeto(value: String): EstadoObjeto =
        try {
            EstadoObjeto.valueOf(value)
        } catch (e: Exception) {
            EstadoObjeto.DISPONIBLE
        }

    // ✅ NUEVO: aprobar solicitud y vincular objeto
    fun aprobarSolicitudYVincularObjeto(solicitud: Solicitud, objeto: ObjetoEncontrado) {
        val solicitudRef = db.collection("solicitudes").document(solicitud.key)
        val objetoRef = db.collection("objetos").document(objeto.key)

        solicitud.estado = EstadoSolicitud.APROBADA
        solicitud.objetoId = objeto.key
        solicitud.codigoObjetoAsignado = objeto.id

        objeto.estado = EstadoObjeto.ASIGNADO
        objeto.solicitudAsignadaId = solicitud.key
        objeto.codigoSolicitudAsignada = solicitud.id

        solicitudRef.set(solicitud)
            .addOnSuccessListener {
                Log.d("SolicitudViewModel", "Solicitud aprobada correctamente")

                objetoRef.set(objeto)
                    .addOnSuccessListener {
                        Log.d("SolicitudViewModel", "Objeto vinculado correctamente")
                    }
                    .addOnFailureListener { e ->
                        Log.w("SolicitudViewModel", "Error al actualizar objeto", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al actualizar solicitud", e)
            }
    }

    fun rechazarSolicitud(solicitud: Solicitud) {
        val solicitudRef = db.collection("solicitudes").document(solicitud.key)
        solicitud.estado = EstadoSolicitud.RECHAZADA

        solicitudRef.set(solicitud)
            .addOnSuccessListener {
                Log.d("SolicitudViewModel", "Solicitud rechazada correctamente")

                // Si hay un objeto vinculado, se desasocia
                val objetoKey = solicitud.objetoId
                if (!objetoKey.isNullOrBlank()) {
                    val objetoRef = db.collection("objetos").document(objetoKey)

                    objetoRef.get().addOnSuccessListener { doc ->
                        if (doc.exists()) {
                            val objeto = doc.toObject(ObjetoEncontrado::class.java)
                            if (objeto != null) {
                                objeto.estado = EstadoObjeto.DISPONIBLE
                                objeto.solicitudAsignadaId = null
                                objeto.codigoSolicitudAsignada = null

                                objetoRef.set(objeto)
                                    .addOnSuccessListener {
                                        Log.d("SolicitudViewModel", "Objeto liberado correctamente")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("SolicitudViewModel", "Error al liberar objeto", e)
                                    }
                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.w("SolicitudViewModel", "Error al consultar objeto vinculado", e)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al rechazar solicitud", e)
            }
    }

    fun entregarSolicitud(solicitud: Solicitud) {
        val solicitudRef = db.collection("solicitudes").document(solicitud.key)
        solicitud.estado = EstadoSolicitud.ENTREGADA

        solicitudRef.set(solicitud)
            .addOnSuccessListener {
                Log.d("SolicitudViewModel", "Solicitud marcada como ENTREGADA")

                val objetoKey = solicitud.objetoId
                if (!objetoKey.isNullOrBlank()) {
                    val objetoRef = db.collection("objetos").document(objetoKey)

                    objetoRef.get().addOnSuccessListener { doc ->
                        if (doc.exists()) {
                            val objeto = doc.toObject(ObjetoEncontrado::class.java)
                            if (objeto != null) {
                                objeto.estado = EstadoObjeto.ENTREGADO
                                // No se eliminan los IDs asignados por trazabilidad

                                objetoRef.set(objeto)
                                    .addOnSuccessListener {
                                        Log.d("SolicitudViewModel", "Objeto marcado como ENTREGADO")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("SolicitudViewModel", "Error al actualizar objeto", e)
                                    }
                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.w("SolicitudViewModel", "Error al consultar objeto vinculado", e)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al actualizar solicitud como ENTREGADA", e)
            }
    }
    fun getSolicitudById(id: String): Solicitud? {
        return solicitudes.value.find { it.id == id || it.key == id }
    }

    fun getObjetoById(id: String): ObjetoEncontrado? {
        return objetosEncontrados.value.find { it.id == id || it.key == id }
    }

    fun cancelarSolicitud(solicitud: Solicitud) {
        val solicitudRef = db.collection("solicitudes").document(solicitud.key)
        solicitud.estado = EstadoSolicitud.CANCELADA

        solicitudRef.set(solicitud)
            .addOnSuccessListener {
                Log.d("SolicitudViewModel", "Solicitud cancelada correctamente")
                // Actualiza la lista en memoria
                _solicitudes.value = _solicitudes.value.map {
                    if (it.key == solicitud.key) solicitud else it
                }
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al cancelar solicitud", e)
            }
    }

    fun obtenerCoincidencias(
        solicitud: Solicitud,
        objetos: List<ObjetoEncontrado>
    ): List<ObjetoEncontrado> {
        return objetos
            .filter { it.estado.name == "DISPONIBLE" && it.categoria == solicitud.categoria }
            .map { it to calcularPuntajeCoincidencia(solicitud, it) }
            .onEach { (objeto, puntaje) ->
                Log.d("CoincidenciaDebug", "Objeto ${objeto.nombre} → Puntaje: $puntaje")
            }
            .sortedByDescending { it.second }
            .map { it.first }
    }


    private fun calcularPuntajeCoincidencia(solicitud: Solicitud, objeto: ObjetoEncontrado): Int {
        var puntaje = 0
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            val fechaSolicitud = formato.parse(solicitud.fecha)
            val fechaObjeto = formato.parse(objeto.fecha)

            if (fechaSolicitud != null && fechaObjeto != null) {
                val diffMs = kotlin.math.abs(fechaSolicitud.time - fechaObjeto.time)
                val diffDias = (diffMs / (1000 * 60 * 60 * 24)).toInt()

                puntaje += when {
                    diffDias == 0 -> 30
                    diffDias == 1 -> 20
                    diffDias <= 2 -> 10
                    else -> 0
                }
            }
        } catch (e: Exception) {
        }
        if (solicitud.lugar.equals(objeto.lugar, ignoreCase = true)) puntaje += 30
        if (solicitud.color.equals(objeto.color, ignoreCase = true)) puntaje += 20
        // Categoría ya fue filtrada antes
        return puntaje
    }



}
