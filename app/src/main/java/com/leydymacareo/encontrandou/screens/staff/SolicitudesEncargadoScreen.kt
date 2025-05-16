package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.leydymacareo.encontrandou.R
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leydymacareo.encontrandou.NavRoutes

@Composable
fun SolicitudesEncargadoScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val solicitudes = listOf(
        Triple("USB Blanca", "1 Abril 2024", "En espera"),
        Triple("Mochila azul", "20 de Febrero 2024", "Aprobada"),
        Triple("Billetera roja", "1 Noviembre 2023", "Con coincidencia"),
        Triple("Llaves de carro", "1 Marzo 2024", "Entregado"),
        Triple("Termo de Agua", "14 Marzo 2024", "Rechazado")
    )

    val estadoColores = mapOf(
        "En espera" to Color.Gray,
        "Aprobada" to Color(0xFF2E7D32),
        "Con coincidencia" to Color(0xFFFB8C00),
        "Entregado" to Color(0xFF6A1B9A),
        "Rechazado" to Color(0xFFD32F2F)
    )

    val estadoIconos = mapOf(
        "En espera" to R.drawable.accesstime,
        "Aprobada" to R.drawable.thumbup,
        "Con coincidencia" to R.drawable.wbsunny,
        "Entregado" to R.drawable.done,
        "Rechazado" to R.drawable.close
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
                    text = "Solicitudes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filterlist),
                        contentDescription = "Filtros"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Filtros")
                }
            }
        },
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
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EncargadoNavItem("Inventario", R.drawable.inventory, currentRoute == NavRoutes.EncargadoHome) {
                        if (currentRoute != NavRoutes.EncargadoHome) navController.navigate(NavRoutes.EncargadoHome)
                    }
                    EncargadoNavItem("Solicitudes", R.drawable.list, currentRoute == NavRoutes.EncargadoSolicitudes) {
                        if (currentRoute != NavRoutes.EncargadoSolicitudes) navController.navigate(NavRoutes.EncargadoSolicitudes)
                    }
                    EncargadoNavItem("Perfil", R.drawable.person, currentRoute == NavRoutes.EncargadoProfile) {
                        if (currentRoute != NavRoutes.UserProfile) navController.navigate(NavRoutes.EncargadoProfile)
                    }
                    EncargadoNavItem("Ajustes", R.drawable.settings, currentRoute == NavRoutes.EncargadoAjustes) {
                        if (currentRoute != NavRoutes.EncargadoAjustes) navController.navigate(NavRoutes.EncargadoAjustes)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())

        ) {
            Spacer(modifier = Modifier.height(12.dp))

            solicitudes.forEach { (nombre, fecha, estado) ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = estadoIconos[estado] ?: R.drawable.help),
                            contentDescription = estado,
                            tint = estadoColores[estado] ?: Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(nombre, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(fecha, color = Color.Black, fontSize = 13.sp)
                            Text(
                                estado.replaceFirstChar { it.uppercase() },
                                color = estadoColores[estado] ?: Color.Gray,
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



