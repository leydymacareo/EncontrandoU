package com.leydymacareo.encontrandou.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel

@Composable
fun SolicitudDetailScreen(
    solicitudId: String,
    rol: String,
    viewModel: SolicitudViewModel,
    navController: NavController
) {
    val solicitud = viewModel.obtenerPorId(solicitudId)

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

                        Spacer(modifier = Modifier.height(24.dp))

                        InfoRow("Código de la Solicitud", solicitud.id)
                        InfoRow("Fecha de la Solicitud", solicitud.fecha)
                        InfoRow("Nombre del objeto", solicitud.nombreObjeto)
                        InfoRow("Lugar de la pérdida", solicitud.lugar)
                        InfoRow("Descripción Adicional", solicitud.descripcion)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Imagen del objeto", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        solicitud.imagenUri?.let { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = "Imagen del objeto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            )
                        } ?: Text("Sin imagen proporcionada", fontSize = 14.sp)

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { /* TODO: Cancelar solicitud */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancelar Solicitud", color = Color.White)
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
