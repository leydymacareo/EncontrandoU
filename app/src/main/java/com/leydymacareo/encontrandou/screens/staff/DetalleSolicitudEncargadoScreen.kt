package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.background
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
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel

@Composable
fun DetalleSolicitudEncargadoScreen(
    solicitudId: String,
    viewModel: SolicitudViewModel,
    navController: NavController
) {
    val solicitudes by viewModel.solicitudes.collectAsState()

    // Si aún no se han cargado solicitudes, las cargamos
    LaunchedEffect(Unit) {
        if (solicitudes.isEmpty()) {
            viewModel.cargarSolicitudesDesdeFirestore()
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

                    Spacer(modifier = Modifier.width(48.dp)) // para balancear el ícono
                }
            }
        },
        content = { innerPadding ->
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
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFFCE7B2))
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🕒 ${solicitud.estado}",
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        }

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
                        InfoRow(
                            "Marca o Modelo",
                            if (solicitud.descripcion.isBlank()) "No especificado" else solicitud.descripcion
                        )
                        InfoRow("Color Principal", solicitud.color)
                        InfoRow("Descripción Adicional", solicitud.descripcion)

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    )
}


