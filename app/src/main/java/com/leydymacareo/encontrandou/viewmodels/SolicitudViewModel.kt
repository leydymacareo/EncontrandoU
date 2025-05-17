package com.leydymacareo.encontrandou.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.leydymacareo.encontrandou.models.ObjetoEncontrado
import com.leydymacareo.encontrandou.models.Solicitud
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class SolicitudViewModel : ViewModel {

    private val _solicitudes = MutableStateFlow<List<Solicitud>>(emptyList())
    val solicitudes: StateFlow<List<Solicitud>> = _solicitudes

    private val _objetosEncontrados = MutableStateFlow<List<ObjetoEncontrado>>(emptyList())
    val objetosEncontrados: StateFlow<List<ObjetoEncontrado>> = _objetosEncontrados

    private var contadorSolicitudes = 0
    private var contadorObjetos = 0

    private val db = FirebaseFirestore.getInstance()

    constructor() {
        Log.d("SolucitudViewModel", "Construct");
    }

    fun agregarSolicitud(solicitud: Solicitud) {
        _solicitudes.value = _solicitudes.value + solicitud
        Log.d("SolicitudViewModel", "agregarSolicitud: ${solicitud.id}");
        // 🔥 Guardar en Firestore
        db.collection("solicitudes")
            .add(solicitud).addOnSuccessListener { documentReference ->
                Log.d("SolicitudViewModel::agregarSolicitud", "DocumentSnapshot written: ${documentReference.id}");
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel::agregarSolicitud", "Error adding document", e)
            }
    }

    fun agregarObjeto(objeto: ObjetoEncontrado) {
        _objetosEncontrados.value = _objetosEncontrados.value + objeto

        // 🔥 Guardar en Firestore
        db.collection("objetos")
            .document(objeto.id)
            .set(objeto)

        Log.d("SolicitudViewModel", "agregarObjeto: ${objeto.id}");
        // 🔥 Guardar en Firestore
        db.collection("objetos")
            .add(objeto).addOnSuccessListener { documentReference ->
                Log.d("SolicitudViewModel::agregarObjeto", "DocumentSnapshot written: ${documentReference.id}");
            }
            .addOnFailureListener { e ->
                Log.w("SolicitudViewModel::agregarObjeto", "Error adding document", e)
            }
    }

    fun obtenerPorId(id: String): Solicitud? {
        return _solicitudes.value.find { it.id == id }
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
        Log.d("SolucitudViewModel", "cargarSolicitudesDesdeFirestore");
        db.collection("solicitudes")
            .get()
            .addOnSuccessListener { result ->
                val listaSolicitudes = result.mapNotNull { doc ->
                    doc.toObject(Solicitud::class.java)
                }
                _solicitudes.value = listaSolicitudes
            }
            .addOnFailureListener {e ->
                // Puedes loguear el error aquí si quieres
                Log.w("SolucitudViewModel", "Error cargarSolicitudesDesdeFirestore", e)
            }
    }

    fun cargarObjetosDesdeFirestore() {
        db.collection("objetos")
            .get()
            .addOnSuccessListener { result ->
                val listaObjetos = result.mapNotNull { doc ->
                    doc.toObject(ObjetoEncontrado::class.java)
                }
                _objetosEncontrados.value = listaObjetos
            }
            .addOnFailureListener {
                // Puedes loguear el error aquí si quieres
            }
    }

    fun cargarSolicitudesDeUsuario(email: String) {
        Log.d("SolucitudViewModel", "Start cargarSolicitudesDeUsuario $email");
        db.collection("solicitudes")
            .whereEqualTo("propietario", email) // 👈 Este campo debe contener el email
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    doc.toObject(Solicitud::class.java)
                }
                _solicitudes.value = lista
                Log.d("SolucitudViewModel", "Complete cargarSolicitudesDeUsuario $email");
            }
            .addOnFailureListener { e ->
                // Puedes loguear el error aquí si quieres
                Log.w("SolucitudViewModel", "Error cargarSolicitudesDeUsuario", e)
            }
    }
}
