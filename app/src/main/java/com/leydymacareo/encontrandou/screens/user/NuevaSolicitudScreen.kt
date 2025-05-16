package com.leydymacareo.encontrandou.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leydymacareo.encontrandou.components.FormularioObjeto
import com.leydymacareo.encontrandou.models.Solicitud
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel
import com.leydymacareo.encontrandou.viewmodel.SessionState
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaSolicitudScreen(
    navController: NavController,
    viewModel: SolicitudViewModel = viewModel(),
    sessionViewModel: SessionViewModel = viewModel()
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva Solicitud", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        when (sessionState) {
            is SessionState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SessionState.LoggedIn -> {
                val user = sessionState as SessionState.LoggedIn

                FormularioObjeto(
                    titulo = "Nueva Solicitud",
                    textoBoton = "Enviar Solicitud",
                    imagenObligatoria = false,
                    onSubmit = { formData, imageUri ->
                        val nuevaSolicitud = Solicitud(
                            id = viewModel.generarCodigoSolicitud(),
                            nombreObjeto = formData.nombreObjeto,
                            propietario = user.name, // 👈 Aquí sí se garantiza que sea el nombre real
                            fecha = formData.fecha,
                            hora = formData.hora,
                            categoria = formData.categoria,
                            color = formData.color,
                            estado = "En Espera",
                            lugar = formData.lugar,
                            descripcion = formData.descripcion,
                            imagenUri = imageUri?.toString()
                        )

                        viewModel.agregarSolicitud(nuevaSolicitud)
                        navController.popBackStack()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                Text("Sesión no iniciada.", modifier = Modifier.padding(20.dp))
            }
        }
    }
}
