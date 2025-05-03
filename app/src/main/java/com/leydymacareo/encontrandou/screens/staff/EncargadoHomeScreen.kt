package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel

@Composable
fun EncargadoHomeScreen(navController: NavController,
                        sessionViewModel: SessionViewModel
) {
    Scaffold(
        topBar = {
            Surface(shadowElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Hola, Mónica", fontWeight = FontWeight.Normal)
                        Text("Inventario", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Image(
                        painter = painterResource(id = R.drawable.logo_unab),
                        contentDescription = "Logo UNAB",
                        modifier = Modifier.height(48.dp)
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFFA9E3F7)
            ) {
                BottomBarItem(icon = Icons.Default.Lock, label = "Inventario")
                BottomBarItem(icon = Icons.Default.List, label = "Solicitudes")
                BottomBarItem(icon = Icons.Default.Person, label = "Perfil")
                BottomBarItem(icon = Icons.Default.Settings, label = "Ajustes")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Acción para agregar item */ },
                containerColor = Color(0xFFFF9900),
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Filtros", color = Color.Gray)
                Text("Estadísticas", color = Color.Gray)
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(4) { index ->
                    InventoryItemCard(
                        title = when (index) {
                            0 -> "USB Blanca"
                            1 -> "Portátil HP"
                            2 -> "Billetera roja"
                            else -> "Bolso Azul"
                        },
                        code = "OBJ-000${index + 1}",
                        date = when (index) {
                            0 -> "1 Febrero 2024"
                            1 -> "20 Febrero 2024"
                            2 -> "4 Marzo 2025"
                            else -> "18 Marzo 2025"
                        },
                        status = when (index) {
                            0, 1 -> "Disponible"
                            2 -> "Asignado"
                            else -> "Entregado"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InventoryItemCard(title: String, code: String, date: String, status: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDFF4FF))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(code, fontSize = 12.sp, color = Color.DarkGray)
            Text(date, fontSize = 12.sp, color = Color.DarkGray)
            Text(status, fontSize = 12.sp, color = when (status) {
                "Disponible" -> Color(0xFF00C853)
                "Asignado" -> Color(0xFFFF5252)
                "Entregado" -> Color(0xFF2962FF)
                else -> Color.Gray
            })
        }
    }
}

@Composable
fun BottomBarItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = Color.Black)
        Text(text = label, fontSize = 12.sp, color = Color.Black)
    }
}
