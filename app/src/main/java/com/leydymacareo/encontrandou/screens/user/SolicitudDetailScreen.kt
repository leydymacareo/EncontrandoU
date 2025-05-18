package com.leydymacareo.encontrandou.screens.user

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.leydymacareo.encontrandou.components.EstadoBadge
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel

@Composable
fun SolicitudDetailScreen(
    solicitudId: String,
    rol: String,
    viewModel: SolicitudViewModel,
    navController: NavController
) {
    Log.d("SolicitudDetailScreen", "solicitudId: $solicitudId")
    val solicitud = viewModel.obtenerSolicitudPorId(solicitudId)

    if (solicitud == null) {
        Text("No se encontró la solicitud.")
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

                    Spacer(modifier = Modifier.width(48.dp)) // para balancear el icono
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

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            EstadoBadge(estado = solicitud.estado.name)
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


                        Spacer(modifier = Modifier.height(24.dp))

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

                        Spacer(modifier = Modifier.height(16.dp))



                        Spacer(modifier = Modifier.height(24.dp))

                        if (solicitud.estado == EstadoSolicitud.PENDIENTE) {
                            var showDialog by remember { mutableStateOf(false) }

                            Button(
                                onClick = { showDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Cancelar Solicitud", color = Color.White)
                            }

                            if (showDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false },
                                    title = { Text("¿Cancelar solicitud?") },
                                    text = { Text("¿Estás seguro de que deseas cancelar esta solicitud?") },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                viewModel.cancelarSolicitud(solicitud)
                                                showDialog = false
                                                navController.popBackStack()
                                            }
                                        ) {
                                            Text("Sí")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = { showDialog = false }) {
                                            Text("No")
                                        }
                                    }
                                )
                            }
                        }

                    }
                }
            }
        }
    )
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Text(text = value, modifier = Modifier.weight(1f))
    }
}
