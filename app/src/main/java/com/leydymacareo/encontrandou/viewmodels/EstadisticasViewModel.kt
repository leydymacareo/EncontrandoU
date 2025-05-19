package com.leydymacareo.encontrandou.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstadisticasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _selectedMonth = MutableStateFlow("Abril")
    val selectedMonth: StateFlow<String> = _selectedMonth

    private val _selectedTipoRegistro = MutableStateFlow("solicitudes")
    val selectedTipoRegistro: StateFlow<String> = _selectedTipoRegistro

    private val _selectedEstado = MutableStateFlow<String?>(null)
    val selectedEstado: StateFlow<String?> = _selectedEstado

    private val _documentosFiltrados = MutableStateFlow<QuerySnapshot?>(null)
    val documentosFiltrados: StateFlow<QuerySnapshot?> = _documentosFiltrados

    fun setMes(value: String) {
        _selectedMonth.value = value
    }

    fun setTipoRegistro(value: String) {
        _selectedTipoRegistro.value = value
    }

    fun setEstado(value: String?) {
        _selectedEstado.value = value
    }

    fun cargarDatosFiltrados() {
        viewModelScope.launch {
            val coleccion = if (_selectedTipoRegistro.value == "solicitudes") "solicitudes" else "objetos"
            val ref = db.collection(coleccion)
                .whereEqualTo("mes", _selectedMonth.value)

            val consulta = if (!selectedEstado.value.isNullOrBlank() && coleccion == "solicitudes") {
                ref.whereEqualTo("estado", selectedEstado.value)
            } else {
                ref
            }

            consulta.get()
                .addOnSuccessListener { result ->
                    _documentosFiltrados.value = result
                }
                .addOnFailureListener {
                    _documentosFiltrados.value = null
                }
        }
    }
}
