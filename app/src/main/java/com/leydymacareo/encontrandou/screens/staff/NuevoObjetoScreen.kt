package com.leydymacareo.encontrandou.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import com.leydymacareo.encontrandou.components.FormularioObjeto
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leydymacareo.encontrandou.components.obtenerFechaActual
import com.leydymacareo.encontrandou.models.ObjetoEncontrado
import com.leydymacareo.encontrandou.viewmodel.SessionState
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoObjetoScreen(
    navController: NavController,
    viewModel: SolicitudViewModel,
    sessionViewModel: SessionViewModel
) {

    val sessionState by sessionViewModel.sessionState.collectAsState()
    val sessionId = (sessionState as? SessionState.LoggedIn)?.sessionId ?: ""

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nuevo Objeto", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = { /* acción volver */ }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        FormularioObjeto(
            titulo = "Nuevo Objeto",
            textoBoton = "Registrar Objeto",
            imagenObligatoria = true,
            onSubmit = { formData, imageUri ->
                val nuevoObjeto = ObjetoEncontrado(
                    id = viewModel.generarCodigoObjeto(sessionId),
                    nombre = formData.nombreObjeto,
                    fecha = obtenerFechaActual(),
                    imagenUri = imageUri?.toString(),
                    descripcion = formData.descripcion,
                    categoria = formData.categoria,
                    color = formData.color,
                    marca = formData.marca,
                    lugar = formData.lugar,
                    fechaAproximada = formData.fecha,
                    horaAproximada = formData.hora
                )

                viewModel.agregarObjeto(nuevoObjeto)
                navController.popBackStack()

            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}
