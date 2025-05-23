package com.leydymacareo.encontrandou.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.leydymacareo.encontrandou.viewmodels.EstadisticasViewModel

@Composable
fun EstadisticasScreen(navController: NavController, viewModel: EstadisticasViewModel) {
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
                        text = "Estadísticas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Left
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Aquí agregaremos los filtros, botón, gráficos y KPIs
            // Listas de opciones
            val meses = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
            val tipos = listOf("Objetos", "Solicitudes")
            val estados = listOf("pendiente", "aprobada", "rechazada", "entregada")

            val selectedMes by viewModel.selectedMonth.collectAsState()
            val selectedTipo by viewModel.selectedTipoRegistro.collectAsState()
            val selectedEstado by viewModel.selectedEstado.collectAsState()
            val anios = listOf("2023", "2024", "2025")
            val selectedAnio by viewModel.selectedYear.collectAsState()
            val estadosSolicitud = listOf("PENDIENTE", "APROBADA", "RECHAZADA", "ENTREGADA", "CANCELADA")
            val estadosObjeto = listOf("DISPONIBLE", "ASIGNADO", "ENTREGADO")

            Row(modifier = Modifier.fillMaxWidth()) {
                DropdownFiltroModern(
                    label = "Año",
                    opciones = anios,
                    seleccionado = selectedAnio,
                    onSeleccionado = { viewModel.setAnio(it) },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                DropdownFiltroModern(
                    label = "Mes",
                    opciones = meses,
                    seleccionado = selectedMes,
                    onSeleccionado = { viewModel.setMes(it) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                DropdownFiltroModern(
                    label = "Tipo de registro",
                    opciones = tipos,
                    seleccionado = selectedTipo,
                    onSeleccionado = { viewModel.setTipoRegistro(it) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                val estadosSegunTipo = when (selectedTipo) {
                    "Solicitudes" -> estadosSolicitud
                    "Objetos" -> estadosObjeto
                    else -> emptyList()
                }

                DropdownFiltroModern(
                    label = "Estado (opcional)",
                    opciones = listOf("") + estadosSegunTipo,
                    seleccionado = selectedEstado ?: "",
                    onSeleccionado = { viewModel.setEstado(if (it.isBlank()) null else it.uppercase()) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }



            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.cargarDatosFiltrados() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AFF1))
            ) {
                Text("Consultar", fontSize = 18.sp, color = Color.White)
            }


            val documentos = viewModel.documentosFiltrados.collectAsState().value

            documentos?.let { snapshot ->
                val total = snapshot.size()
                val estadosCount = snapshot.groupingBy { it.getString("estado") ?: "Sin estado" }.eachCount()

                val hayFiltroEstado = !selectedEstado.isNullOrBlank()
                if (hayFiltroEstado) {
                    Text(
                        text = "Mostrando resultados filtrados por estado: ${selectedEstado}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Resumen del mes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    EstadisticaKpi("Total registros encontrados", "$total")

                    if (estadosCount.isNotEmpty()) {
                        val hayFiltroEstado = !selectedEstado.isNullOrBlank()
                        if (selectedTipo == "Solicitudes") {
                            if (!hayFiltroEstado) {
                                val pendientes = estadosCount["PENDIENTE"] ?: 0
                                val aprobadas = estadosCount["APROBADA"] ?: 0
                                val rechazadas = estadosCount["RECHAZADA"] ?: 0
                                val entregadas = estadosCount["ENTREGADA"] ?: 0
                                val canceladas = estadosCount["CANCELADA"] ?: 0

                                EstadisticaKpi("Pendientes / Aprobadas / Rechazadas", "$pendientes / $aprobadas / $rechazadas")
                                EstadisticaKpi("Entregadas", "$entregadas")
                                EstadisticaKpi("Canceladas", "$canceladas")

                                val porcentajeExito = if (total > 0) (aprobadas.toFloat() / total * 100).toInt() else 0
                                EstadisticaKpi("Porcentaje de éxito", "$porcentajeExito%")
                            } else {
                                EstadisticaKpi("Total solicitudes ${selectedEstado?.lowercase()?.replaceFirstChar { it.uppercase() }}", "$total")
                            }
                        } else if (selectedTipo == "Objetos") {
                            if (!hayFiltroEstado) {
                                val disponibles = estadosCount["DISPONIBLE"] ?: 0
                                val asignados = estadosCount["ASIGNADO"] ?: 0
                                val entregados = estadosCount["ENTREGADO"] ?: 0

                                EstadisticaKpi("Disponibles / Asignados / Entregados", "$disponibles / $asignados / $entregados")

                                val porcentajeAsignados = if (total > 0) (asignados.toFloat() / total * 100).toInt() else 0
                                val porcentajeEntregados = if (total > 0) (entregados.toFloat() / total * 100).toInt() else 0

                                EstadisticaKpi("Porcentaje asignados", "$porcentajeAsignados%")
                                EstadisticaKpi("Porcentaje entregados", "$porcentajeEntregados%")
                            } else {
                                EstadisticaKpi("Total objetos ${selectedEstado?.lowercase()?.replaceFirstChar { it.uppercase() }}", "$total")
                            }
                        }


                        val categoriasCount = snapshot.groupingBy { it.getString("categoria") ?: "Sin categoría" }
                            .eachCount()
                            .toList()
                            .sortedByDescending { it.second }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Distribución por categoría", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                PieChart(data = categoriasCount)
                                Spacer(modifier = Modifier.height(12.dp))
                                LeyendaPie(data = categoriasCount)
                            }
                        }


                    }

                }
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFiltroModern(
    label: String,
    opciones: List<String>,
    seleccionado: String,
    onSeleccionado: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.padding(vertical = 6.dp)
    ) {
        OutlinedTextField(
            readOnly = true,
            value = seleccionado,
            onValueChange = {},
            label = { Text(label, fontSize = 14.sp) },
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSeleccionado(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun EstadisticaKpi(titulo: String, valor: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(titulo, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Text(valor, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun PieChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier,
    radius: Dp = 100.dp
) {
    val total = data.sumOf { it.second }
    val proportions = data.map { it.second.toFloat() / total }
    val angles = proportions.map { it * 360f }

    val colors = listOf(
        Color(0xFFEF5350), Color(0xFFAB47BC), Color(0xFF42A5F5),
        Color(0xFF66BB6A), Color(0xFFFFA726), Color(0xFF26C6DA),
        Color(0xFFFFD54F), Color(0xFF8D6E63), Color(0xFF7E57C2),
        Color(0xFF26A69A), Color(0xFFFF7043), Color(0xFF78909C)
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(radius * 2)
            .padding(8.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        var startAngle = -90f
        angles.forEachIndexed { index, sweepAngle ->
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = androidx.compose.ui.geometry.Offset(
                    centerX - radius.toPx(),
                    centerY - radius.toPx()
                ),
                size = androidx.compose.ui.geometry.Size(
                    width = radius.toPx() * 2,
                    height = radius.toPx() * 2
                )
            )
            startAngle += sweepAngle
        }
    }


    Spacer(modifier = Modifier.height(8.dp))



}
@Composable
fun LeyendaPie(data: List<Pair<String, Int>>) {
    val colors = listOf(
        Color(0xFFEF5350), Color(0xFFAB47BC), Color(0xFF42A5F5),
        Color(0xFF66BB6A), Color(0xFFFFA726), Color(0xFF26C6DA)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        data.forEachIndexed { index, (label, count) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(colors[index % colors.size], shape = RoundedCornerShape(3.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("$label: $count", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
