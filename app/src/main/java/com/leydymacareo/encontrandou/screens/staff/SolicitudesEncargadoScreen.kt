package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.leydymacareo.encontrandou.R
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel

@Composable
fun SolicitudesEncargadoScreen(
    navController: NavController,
    viewModel: SolicitudViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val solicitudes by viewModel.solicitudes.collectAsState()

    // ✅ Cargar solicitudes al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarSolicitudesDesdeFirestore()
    }

    val estadoColores = mapOf(
        "En Espera" to Color.Gray,
        "Aprobada" to Color(0xFF2E7D32),
        "Con Coincidencia" to Color(0xFFFB8C00),
        "Entregado" to Color(0xFF6A1B9A),
        "Rechazada" to Color(0xFFD32F2F)
    )

    val estadoIconos = mapOf(
        "En Espera" to R.drawable.accesstime,
        "Aprobada" to R.drawable.thumbup,
        "Con Coincidencia" to R.drawable.wbsunny,
        "Entregado" to R.drawable.done,
        "Rechazada" to R.drawable.close
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
                        if (currentRoute != NavRoutes.EncargadoProfile) navController.navigate(NavRoutes.EncargadoProfile)
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

            solicitudes.forEach { solicitud ->
                val estado = solicitud.estado
                val color = estadoColores[estado] ?: Color.Gray
                val icono = estadoIconos[estado] ?: R.drawable.help

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            navController.navigate("detalle_solicitud_encargado/${solicitud.id}")
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = icono),
                            contentDescription = estado,
                            tint = color,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(solicitud.nombreObjeto, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(solicitud.fecha, color = Color.Black, fontSize = 13.sp)
                            Text(
                                estado.replaceFirstChar { it.uppercase() },
                                color = color,
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
