package com.leydymacareo.encontrandou.screens.home

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.screens.prueba.BottomNavItem


@Composable
fun HomeScreenUsuario() {
    var selectedTab by remember { mutableStateOf("Inicio") }

    val items = listOf(
        Triple("USB Blanca", "1 Abril 2024", "En Espera"),
        Triple("Mochila azul", "20 de Febrero 2024", "Aprobada"),
        Triple("Billetera roja", "1 Noviembre 2023", "Entregada"),
        Triple("Llaves de carro", "1 Marzo 2024", "Rechazada")
    )

    val statusIcons = mapOf(
        "Aprobada" to Icons.Default.ThumbUp,
        "Entregada" to Icons.Default.Check,
        "Rechazada" to Icons.Default.Close
    )

    val statusColors = mapOf(
        "En Espera" to Color(0xFFFFA000),
        "Aprobada" to Color(0xFF2E7D32),
        "Entregada" to Color(0xFF1565C0),
        "Rechazada" to Color(0xFFD32F2F)
    )

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(top = 40.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Mis Solicitudes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Acción del FAB */ },
                containerColor = Color(0xFFFF9900),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva solicitud", tint = Color.White)
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
                    BottomNavItem("Inicio", Icons.Default.Home, selectedTab) { selectedTab = it }
                    NavBarItem(icon = painterResource(id = R.drawable.help), label = "Ayuda")
                    BottomNavItem("Perfil", Icons.Default.Person, selectedTab) { selectedTab = it }
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
                        if (estado == "En Espera") {
                            Icon(
                                painter = painterResource(id = R.drawable.accesstime),
                                contentDescription = estado,
                                tint = statusColors[estado] ?: Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            Icon(
                                imageVector = statusIcons[estado] ?: Icons.Default.Info,
                                contentDescription = estado,
                                tint = statusColors[estado] ?: Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(text = nombre, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(text = fecha, color = Color.Black, fontSize = 13.sp)
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
fun NavBarItem(icon: Painter, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painter = icon, contentDescription = label, tint = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Black)
    }
}

@Composable
fun NavBarItem(icon: ImageVector, label: String) {
    NavBarItem(icon = rememberVectorPainter(image = icon), label = label)
}
