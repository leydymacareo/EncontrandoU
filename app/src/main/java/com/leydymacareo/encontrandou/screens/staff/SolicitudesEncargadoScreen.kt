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
import com.leydymacareo.encontrandou.components.EstadoBadge

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
                        Column {
                            Text(solicitud.nombreObjeto, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(text = solicitud.id, fontSize = 13.sp, color = Color.Black)
                            Text(solicitud.fecha, color = Color.Black, fontSize = 13.sp)
                            EstadoBadge(estado = solicitud.estado.name)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
