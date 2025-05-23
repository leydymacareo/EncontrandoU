package com.leydymacareo.encontrandou.viewmodels

import EstadoObjeto
import EstadoSolicitud
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.leydymacareo.encontrandou.models.ObjetoEncontrado
import com.leydymacareo.encontrandou.models.Solicitud
import com.leydymacareo.encontrandou.utils.obtenerAnioDesdeFecha
import com.leydymacareo.encontrandou.utils.obtenerMesDesdeFecha
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

    private val storageRepository = StorageRepository()

    init {
        Log.d("SolicitudViewModel", "SolicitudViewModel iniciado")
    }


    fun agregarSolicitud(solicitud: Solicitud) {
        _solicitudes.value = _solicitudes.value + solicitud

        // Obtener mes y año desde la fecha de registro
        val mes = obtenerMesDesdeFecha(solicitud.fecha)
        val año = obtenerAnioDesdeFecha(solicitud.fecha)

        val solicitudMap = hashMapOf(
            "nombreObjeto" to solicitud.nombreObjeto,
            "propietario" to solicitud.propietario,
            "fechaAproximada" to solicitud.fechaAproximada,
            "fecha" to solicitud.fecha,
            "horaAproximada" to solicitud.horaAproximada,
            "categoria" to solicitud.categoria,
            "color" to solicitud.color,
            "lugar" to solicitud.lugar,
            "descripcion" to solicitud.descripcion,
            "estado" to solicitud.estado.name,
            "imagenUri" to solicitud.imagenUri,
            "id" to solicitud.id,
            "sessionId" to solicitud.sessionId,
            "codigoObjetoAsignado" to solicitud.codigoObjetoAsignado,
            "mes" to mes,
            "año" to año
        )

        db.collection("solicitudes")
            .add(solicitudMap)
            .addOnSuccessListener { docRef ->
                Log.d("SolicitudViewModel", "Solicitud guardada con mes/año: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al guardar solicitud", e)
            }
    }

     fun agregarSolicitudConImagen(context: Context, solicitud: Solicitud, imagenUri: Uri?)
    {
        val id = UUID.randomUUID().toString()

        // Subir imagen si existe
       if (imagenUri != null) {
            storageRepository.subirImagen(context, imagenUri, "solicitudes", id,  onSuccess = { url ->
                Log.d("MyApp", "Upload successful. URL: $url")
                val solicitudConImagen = solicitud.copy(imagenUri = url)

                agregarSolicitud(solicitudConImagen)
            },onError = { error ->
                Log.e("MyApp", "Upload failed", error)

                agregarSolicitud(solicitud)
            })
        } else {
            agregarSolicitud(solicitud)
        }
    }

    fun agregarObjeto(objeto: ObjetoEncontrado) {
        _objetosEncontrados.value = _objetosEncontrados.value + objeto

        // Obtener mes y año desde la fecha de registro (usa la fecha real de creación)
        val mes = obtenerMesDesdeFecha(objeto.fecha)
        val año = obtenerAnioDesdeFecha(objeto.fecha)

        val objetoMap = hashMapOf(
            "nombre" to objeto.nombre,
            "fecha" to objeto.fecha,
            "estado" to objeto.estado.name,
            "imagenUri" to objeto.imagenUri,
            "descripcion" to objeto.descripcion,
            "categoria" to objeto.categoria,
            "color" to objeto.color,
            "marca" to objeto.marca,
            "lugar" to objeto.lugar,
            "fechaAproximada" to objeto.fechaAproximada,
            "horaAproximada" to objeto.horaAproximada,
            "solicitudAsignadaId" to objeto.solicitudAsignadaId,
            "codigoSolicitudAsignada" to objeto.codigoSolicitudAsignada,
            "id" to objeto.id,
            "mes" to mes,
            "año" to año
        )

        db.collection("objetos")
            .add(objetoMap)
            .addOnSuccessListener { docRef ->
                Log.d("SolicitudViewModel", "Objeto guardado con mes/año: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel", "Error al guardar objeto", e)
            }
    }

    fun agregarObjetoConImagen(context: Context, objeto: ObjetoEncontrado, imagenUri: Uri) {
        val id = UUID.randomUUID().toString()

        // Subir imagen a Firebase Storage
        val urlImagen = storageRepository.subirImagen(context, imagenUri, "objetos", id,onSuccess = { url ->
            Log.d("MyApp", "Upload successful. URL: $url")
            val objetoConImagen = objeto.copy(imagenUri = url)

            agregarObjeto(objetoConImagen)
        },onError = { error ->
            Log.e("MyApp", "Upload failed", error)

            agregarObjeto(objeto)
        })
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
                            horaAproximada = doc.getString("horaAproximada") ?: "",
                            fechaAproximada = doc.getString("fechaAproximada") ?: "",
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
                            horaAproximada = doc.getString("horaAproximada") ?: "",
                            fechaAproximada = doc.getString("fechaAproximada") ?: "",
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

    // FUNCIONES DE ENUM SEGURO
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

    // NUEVO: aprobar solicitud y vincular objeto
    fun aprobarSolicitudYVincularObjeto(solicitud: Solicitud, objeto: ObjetoEncontrado) {
        val solicitudRef = db.collection("solicitudes").document(solicitud.key)
        val objetoRef = db.collection("objetos").document(objeto.key)

        solicitud.estado = EstadoSolicitud.APROBADA
        solicitud.objetoId = objeto.key
        solicitud.codigoObjetoAsignado = objeto.id

        objeto.estado = EstadoObjeto.ASIGNADO
        objeto.solicitudAsignadaId = solicitud.key
        objeto.codigoSolicitudAsignada = solicitud.id

        solicitudRef.update(
            mutableMapOf(
                "estado" to EstadoSolicitud.APROBADA,
                "objetoId" to objeto.key,
                "codigoObjetoAsignado" to objeto.id
            ) as Map<String, Any>
        )
            .addOnSuccessListener {
                Log.d("SolicitudViewModel", "Solicitud aprobada correctamente")

                objetoRef.update(
                    mapOf(
                        "estado" to EstadoObjeto.ASIGNADO.name,
                        "solicitudAsignadaId" to solicitud.key,
                        "codigoSolicitudAsignada" to solicitud.id
                    )
                )

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

        solicitudRef.update( hashMapOf<String, Any>(
            "estado" to EstadoSolicitud.RECHAZADA
        ))
            .addOnSuccessListener {
                Log.d("SolicitudViewModel", "Solicitud rechazada correctamente")

                // Si hay un objeto vinculado, se desasocia
                val objetoKey = solicitud.objetoId
                if (!objetoKey.isNullOrBlank()) {
                    val objetoRef = db.collection("objetos").document(objetoKey)

                    objetoRef.update(
                        mapOf(
                            "estado" to EstadoObjeto.DISPONIBLE.name,
                            "solicitudAsignadaId" to null,
                            "codigoSolicitudAsignada" to null
                        )
                    )
                        .addOnSuccessListener {
                            Log.d("SolicitudViewModel", "Objeto liberado correctamente")
                        }
                        .addOnFailureListener { e ->
                            Log.w("SolicitudViewModel", "Error al liberar objeto", e)
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

        solicitudRef.update( hashMapOf<String, Any>(
            "estado" to EstadoSolicitud.ENTREGADA
        ))
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

                                objetoRef.update("estado", EstadoObjeto.ENTREGADO.name)
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

        solicitudRef.update( hashMapOf<String, Any>(
            "estado" to EstadoSolicitud.CANCELADA
        ))
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
            val fechaSolicitud = formato.parse(solicitud.fechaAproximada)
            val fechaObjeto = formato.parse(objeto.fechaAproximada)

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
