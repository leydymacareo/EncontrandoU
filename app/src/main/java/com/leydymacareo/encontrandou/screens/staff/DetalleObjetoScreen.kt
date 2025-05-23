package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel
import androidx.navigation.NavController
import com.leydymacareo.encontrandou.components.EstadoBadge


@Composable
fun DetalleObjetoScreen(
    objetoId: String,
    viewModel: SolicitudViewModel,
    navController: NavController
) {
    val objeto = viewModel.objetosEncontrados.collectAsState().value.find { it.id == objetoId }
    if (objeto == null) {
        Text("Objeto no encontrado", color = Color.Red, modifier = Modifier.padding(16.dp))
        return
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 40.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Detalle del Objeto",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        containerColor = Color.White
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
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EstadoBadge(estado = objeto.estado.name)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Imagen centrada
                    objeto.imagenUri?.let { uri ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "Imagen del objeto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    } ?: Text("Sin imagen proporcionada", fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Información
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

                    if (objeto.estado.name == "ASIGNADO" || objeto.estado.name == "ENTREGADO") {
                        val solicitudAsignada = viewModel.getSolicitudById(objeto.solicitudAsignadaId ?: "")

                        if (solicitudAsignada != null) {
                            Spacer(modifier = Modifier.height(24.dp))

                            val cardColor = if (objeto.estado.name == "ENTREGADO") Color(0xFFAFDDFF) else Color(0xFFDDF6D2)
                            val titleText = if (objeto.estado.name == "ENTREGADO") "Entregado a" else "Asignado a"
                            val titleColor = if (objeto.estado.name == "ENTREGADO") Color(0xFF1565C0) else Color(0xFF2E7D32)

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = cardColor),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = titleText,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = titleColor
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    InfoRow("Correo del Propietario", solicitudAsignada.propietario)
                                    InfoRow("Código de la Solicitud", solicitudAsignada.id)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
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
