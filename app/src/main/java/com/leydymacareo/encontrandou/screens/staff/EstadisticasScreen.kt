package com.leydymacareo.encontrandou.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            // Aquí agregaremos los filtros, botón, gráficos y KPIs
            // Listas de opciones
            val meses = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
            val tipos = listOf("objetos", "solicitudes")
            val estados = listOf("pendiente", "aprobada", "rechazada", "entregada")

            val selectedMes by viewModel.selectedMonth.collectAsState()
            val selectedTipo by viewModel.selectedTipoRegistro.collectAsState()
            val selectedEstado by viewModel.selectedEstado.collectAsState()

// Mes
            DropdownFiltro(
                label = "Mes",
                opciones = meses,
                seleccionado = selectedMes,
                onSeleccionado = { viewModel.setMes(it) }
            )

// Tipo
            DropdownFiltro(
                label = "Tipo de registro",
                opciones = tipos,
                seleccionado = selectedTipo,
                onSeleccionado = { viewModel.setTipoRegistro(it) }
            )

            val estadosSolicitud = listOf("PENDIENTE", "APROBADA", "RECHAZADA", "ENTREGADA", "CANCELADA")
            val estadosObjeto = listOf("DISPONIBLE", "ASIGNADO", "ENTREGADO")

            val estadosSegunTipo = when (selectedTipo) {
                "solicitudes" -> estadosSolicitud
                "objetos" -> estadosObjeto
                else -> emptyList()
            }

            if (estadosSegunTipo.isNotEmpty()) {
                DropdownFiltro(
                    label = "Estado (opcional)",
                    opciones = listOf("") + estadosSegunTipo, // permite limpiar
                    seleccionado = selectedEstado ?: "",
                    onSeleccionado = { viewModel.setEstado(if (it.isBlank()) null else it) }
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.cargarDatosFiltrados() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Consultar")
            }
            val documentos = viewModel.documentosFiltrados.collectAsState().value

            documentos?.let { snapshot ->
                val total = snapshot.size()
                val estadosCount = snapshot.groupingBy { it.getString("estado") ?: "Sin estado" }.eachCount()

                Spacer(modifier = Modifier.height(24.dp))
                Text("Generalidades", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    EstadisticaKpi("Total registros encontrados", "$total")

                    if (estadosCount.isNotEmpty()) {
                        val pendientes = estadosCount["PENDIENTE"] ?: 0
                        val aprobadas = estadosCount["APROBADA"] ?: 0
                        val rechazadas = estadosCount["RECHAZADA"] ?: 0
                        val entregadas = estadosCount["ENTREGADA"] ?: 0

                        EstadisticaKpi("Pendientes / Aprobadas / Rechazadas", "$pendientes / $aprobadas / $rechazadas")
                        EstadisticaKpi("Entregadas", "$entregadas")

                        val porcentajeExito = if (total > 0) (aprobadas.toFloat() / total * 100).toInt() else 0
                        EstadisticaKpi("Porcentaje de éxito", "$porcentajeExito%")

                        val categoriasCount = snapshot.groupingBy { it.getString("categoria") ?: "Sin categoría" }
                            .eachCount()
                            .toList()
                            .sortedByDescending { it.second }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Objetos por Categoría", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        BarChart(data = categoriasCount)

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Categorías más comunes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        PieChart(data = categoriasCount)



                    }

                }
            }


        }
    }
}

@Composable
fun DropdownFiltro(
    label: String,
    opciones: List<String>,
    seleccionado: String,
    onSeleccionado: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Box {
            OutlinedTextField(
                value = seleccionado,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                enabled = false,
                readOnly = true
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
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
fun BarChart(data: List<Pair<String, Int>>, maxBarHeight: Dp = 120.dp) {
    val maxCount = data.maxOfOrNull { it.second } ?: 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxBarHeight + 32.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { (label, value) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height((value / maxCount.toFloat() * maxBarHeight.value).dp)
                        .background(Color(0xFF42A5F5), shape = RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label.take(5), // por si son nombres largos
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
            }
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
        Color(0xFF66BB6A), Color(0xFFFFA726), Color(0xFF26C6DA)
    )

    Canvas(
        modifier = modifier
            .size(radius * 2)
            .padding(8.dp)
    ) {
        var startAngle = -90f
        angles.forEachIndexed { index, sweepAngle ->
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Leyenda
    Column {
        data.forEachIndexed { index, (label, count) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(colors[index % colors.size])
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("$label: $count", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
