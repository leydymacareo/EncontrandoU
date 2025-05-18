package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.components.EstadoBadge
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel

@Composable
fun DetalleSolicitudEncargadoScreen(
    solicitudId: String,
    viewModel: SolicitudViewModel,
    navController: NavController
) {
    val solicitudes by viewModel.solicitudes.collectAsState()
    val objetos by viewModel.objetosEncontrados.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (solicitudes.isEmpty()) {
            viewModel.cargarSolicitudesDesdeFirestore()
        }
        if (objetos.isEmpty()) {
            viewModel.cargarObjetosDesdeFirestore()
        }
    }

    val solicitud = solicitudes.find { it.id == solicitudId }

    if (solicitud == null) {
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
            Surface {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(top = 40.dp, bottom = 20.dp, start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }

                    Text(
                        text = "Detalle de la Solicitud",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Left
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
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
                    Column(modifier = Modifier.padding(20.dp)) {
                        EstadoBadge(estado = solicitud.estado.name)

                        Spacer(modifier = Modifier.height(20.dp))

                        solicitud.imagenUri?.let { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = "Imagen del objeto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .align(Alignment.CenterHorizontally)
                            )
                        } ?: Text(
                            text = "Sin imagen proporcionada",
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        InfoRow("Código de la Solicitud", solicitud.id)
                        InfoRow("Nombre del Propietario", solicitud.propietario)
                        InfoRow("Fecha de la Solicitud", solicitud.fecha)
                        InfoRow("Nombre del objeto", solicitud.nombreObjeto)
                        InfoRow("Lugar de la pérdida", solicitud.lugar)
                        InfoRow("Fecha Aproximada", solicitud.fecha)
                        InfoRow("Hora Aproximada", solicitud.hora)
                        InfoRow("Categoría", solicitud.categoria)
                        InfoRow("Marca o Modelo", if (solicitud.descripcion.isBlank()) "No especificado" else solicitud.descripcion)
                        InfoRow("Color Principal", solicitud.color)
                        InfoRow("Descripción Adicional", solicitud.descripcion)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (solicitud.estado.name == "APROBADA") {
                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Marcar como Entregada", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val disponibles = objetos.filter { it.estado.name == "DISPONIBLE" }

                if (disponibles.isNotEmpty()) {
                    Text(
                        text = "Objetos disponibles para asignar:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    disponibles.forEach { objeto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    navController.navigate(
                                        NavRoutes.detalleObjetoParaAsignarRoute(
                                            solicitudId = solicitud.key,
                                            objetoId = objeto.key
                                        )
                                    )
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = objeto.nombre, fontWeight = FontWeight.Bold)
                                Text(text = "Categoría: ${objeto.categoria}")
                                Text(text = "Color: ${objeto.color}")
                                Text(text = "Lugar: ${objeto.lugar}")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    )

    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("¿Confirmar entrega?") },
            text = { Text("¿Estás seguro de que quieres marcar esta solicitud como entregada?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.entregarSolicitud(solicitud)
                        showConfirmDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
