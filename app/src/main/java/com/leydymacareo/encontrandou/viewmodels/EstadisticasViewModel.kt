package com.leydymacareo.encontrandou.viewmodels

import android.util.Log
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

    private val _selectedYear = MutableStateFlow("2025")
    val selectedYear: StateFlow<String> = _selectedYear

    private val _selectedTipoRegistro = MutableStateFlow("solicitudes")
    val selectedTipoRegistro: StateFlow<String> = _selectedTipoRegistro

    private val _selectedEstado = MutableStateFlow<String?>(null)
    val selectedEstado: StateFlow<String?> = _selectedEstado

    private val _documentosFiltrados = MutableStateFlow<QuerySnapshot?>(null)
    val documentosFiltrados: StateFlow<QuerySnapshot?> = _documentosFiltrados

    fun setMes(value: String) {
        _selectedMonth.value = value
    }

    fun setAnio(value: String) {
        _selectedYear.value = value
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

            var ref = db.collection(coleccion)
                .whereEqualTo("mes", _selectedMonth.value)
                .whereEqualTo("año", _selectedYear.value)

            Log.d("Estadisticas", "Filtrando: tipo=$coleccion, mes=${_selectedMonth.value}, año=${_selectedYear.value}, estado=${_selectedEstado.value}")

            if (!selectedEstado.value.isNullOrBlank() && coleccion == "solicitudes") {
                ref = ref.whereEqualTo("estado", selectedEstado.value)
            }

            ref.get()
                .addOnSuccessListener { result ->
                    Log.d("Estadisticas", "Resultados obtenidos: ${result.size()}")
                    result.documents.forEach {
                        Log.d("Estadisticas", "Doc ID=${it.id} - estado=${it.getString("estado")}")
                    }
                    _documentosFiltrados.value = result
                }
                .addOnFailureListener {
                    Log.e("Estadisticas", "Error al consultar Firestore", it)
                    _documentosFiltrados.value = null
                }
        }
    }

}
