package com.leydymacareo.encontrandou.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConfiguracionViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias

    private val _lugares = MutableStateFlow<List<String>>(emptyList())
    val lugares: StateFlow<List<String>> = _lugares

    private val _colores = MutableStateFlow<List<String>>(emptyList())
    val colores: StateFlow<List<String>> = _colores

    init {
        cargarTodoDesdeFirestore()
    }

    fun cargarTodoDesdeFirestore() {
        cargarDesdeColeccion("categorias") { _categorias.value = it }
        cargarDesdeColeccion("lugares") { _lugares.value = it }
        cargarDesdeColeccion("colores") { _colores.value = it }
    }

    private fun cargarDesdeColeccion(nombre: String, onResult: (List<String>) -> Unit) {
        db.collection(nombre)
            .get()
            .addOnSuccessListener { result ->
                val lista = result
                    .mapNotNull { it.getString("nombre") }
                    .sortedBy { it.lowercase() }
                onResult(lista)
            }
            .addOnFailureListener { e ->
                Log.w("ConfiguracionViewModel", "Error al cargar $nombre", e)
                onResult(emptyList())
            }
    }


    fun agregarElemento(tipo: String, nombre: String) {
        if (nombre.isBlank()) return
        db.collection(tipo).add(mapOf("nombre" to nombre)).addOnSuccessListener {
            cargarTodoDesdeFirestore()
        }
    }

    fun editarElemento(tipo: String, actual: String, nuevo: String) {
        if (nuevo.isBlank()) return
        db.collection(tipo)
            .whereEqualTo("nombre", actual)
            .get()
            .addOnSuccessListener { docs ->
                docs.firstOrNull()?.reference?.set(mapOf("nombre" to nuevo))?.addOnSuccessListener {
                    cargarTodoDesdeFirestore()
                }
            }
    }

    fun eliminarElemento(tipo: String, nombre: String) {
        db.collection(tipo)
            .whereEqualTo("nombre", nombre)
            .get()
            .addOnSuccessListener { docs ->
                docs.firstOrNull()?.reference?.delete()?.addOnSuccessListener {
                    cargarTodoDesdeFirestore()
                }
            }
    }
}
