package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel
import androidx.navigation.NavController

@Composable
fun EncargadoHomeScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    var selectedTab by remember { mutableStateOf("Inventario") }

    val items = listOf(
        Triple("USB Blanca", "1 Febrero 2024", "Disponible"),
        Triple("Portátil HP", "20 de Febrero 2024", "Disponible"),
        Triple("Billetera roja", "4 Marzo 2025", "Asignado"),
        Triple("Bolso Azul", "18 Marzo 2025", "Entregado")
    )

    val statusColors = mapOf(
        "Disponible" to Color(0xFFFFA000),
        "Asignado" to Color(0xFF2E7D32),
        "Entregado" to Color(0xFF1565C0)
    )

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Inventario",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.filterlist),
                            contentDescription = "Filtros"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Filtros")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.barchart),
                            contentDescription = "Estadísticas"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Estadísticas")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = Color(0xFFFF9900),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    EncargadoNavItem("Inventario", painterResource(id = R.drawable.inventory), selectedTab) { selectedTab = it }
                    EncargadoNavItem("Solicitudes", painterResource(id = R.drawable.list), selectedTab) { selectedTab = it }
                    EncargadoNavItem("Perfil", painterResource(id = R.drawable.person), selectedTab) { selectedTab = it }
                    EncargadoNavItem("Ajustes", painterResource(id = R.drawable.settings), selectedTab) { selectedTab = it }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            items.forEach { (nombre, fecha, estado) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.LightGray, RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(text = nombre, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(text = fecha, fontSize = 13.sp, color = Color.Black)
                            Text(
                                text = estado,
                                color = statusColors[estado] ?: Color.Gray,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun EncargadoNavItem(label: String, icon: Painter, selectedTab: String, onClick: (String) -> Unit) {
    val selected = label == selectedTab
    val color = Color.Black
    val fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal

    Column(
        modifier = Modifier
            .clickable { onClick(label) }
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = icon, contentDescription = label, tint = color)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = color, fontWeight = fontWeight)
    }
}
