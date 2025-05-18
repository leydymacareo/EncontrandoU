package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.leydymacareo.encontrandou.components.EstadoBadge
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleObjetoSeleccionableScreen(
    solicitudId: String,
    objetoId: String,
    navController: NavController,
    viewModel: SolicitudViewModel
) {
    val objetos by viewModel.objetosEncontrados.collectAsState()
    val solicitud = viewModel.getSolicitudById(solicitudId)
    val objeto = viewModel.getObjetoById(objetoId)

    // Cargar objetos si aún no están cargados
    LaunchedEffect(Unit) {
        if (objetos.isEmpty()) {
            viewModel.cargarObjetosDesdeFirestore()
        }
    }

    if (solicitud == null || objeto == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            CircularProgressIndicator(color = Color(0xFF00AFF1))
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asignar Objeto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9EC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EstadoBadge(estado = objeto.estado.name)

                    Spacer(modifier = Modifier.height(20.dp))

                    objeto.imagenUri?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "Imagen del objeto",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    } ?: Text("Sin imagen proporcionada", fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    InfoRow("Código del Objeto", objeto.id)
                    InfoRow("Fecha de Registro", objeto.fecha)
                    InfoRow("Nombre del Objeto", objeto.nombre)
                    InfoRow("Lugar de la pérdida", objeto.lugar)
                    InfoRow("Fecha Aproximada", objeto.fechaAproximada)
                    InfoRow("Hora Aproximada", objeto.horaAproximada)
                    InfoRow("Categoría", objeto.categoria)
                    InfoRow("Color Principal", objeto.color)
                    InfoRow("Marca o Modelo", objeto.marca?.ifBlank { "No especificado" } ?: "No especificado")
                    InfoRow("Descripción Adicional", objeto.descripcion.ifBlank { "Sin descripción adicional." })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.aprobarSolicitudYVincularObjeto(solicitud, objeto)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Asignar a esta solicitud", color = Color.White)
            }
        }
    }
}
