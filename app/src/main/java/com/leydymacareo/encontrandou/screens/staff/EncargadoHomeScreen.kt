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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel
import com.leydymacareo.encontrandou.models.ObjetoEncontrado
import com.leydymacareo.encontrandou.viewmodel.SessionState


@Composable
fun EncargadoHomeScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    viewModel: SolicitudViewModel
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val nombre = (sessionState as? SessionState.LoggedIn)?.name ?: "Encargado"
    val correo = (sessionState as? SessionState.LoggedIn)?.email ?: "correo@unab.edu.co"
    val rol = (sessionState as? SessionState.LoggedIn)?.role ?: "Encargado"

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val objetos by viewModel.objetosEncontrados.collectAsState()

    val statusColors = mapOf(
        "Disponible" to Color(0xFFFFA000),
        "Asignado" to Color(0xFF2E7D32),
        "Entregado" to Color(0xFF1565C0)
    )

    // Cargar objetos de firebase
    LaunchedEffect(sessionState) {
        viewModel.cargarObjetosDesdeFirestore()
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
                onClick = {
                    navController.navigate(NavRoutes.NuevoObjeto)
                },
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
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            if (objetos.isEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Aún no has registrado ningún objeto.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                objetos.forEach { objeto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("detalle_objeto/${objeto.id}")
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!objeto.imagenUri.isNullOrBlank()) {
                                AsyncImage(
                                    model = objeto.imagenUri,
                                    contentDescription = "Imagen del objeto",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(text = objeto.nombre, fontWeight = FontWeight.Bold, color = Color.Black)
                                Text(text = objeto.fecha, fontSize = 13.sp, color = Color.Black)
                                Text(
                                    text = objeto.estado,
                                    color = statusColors[objeto.estado] ?: Color.Gray,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}


@Composable
fun EncargadoNavItem(label: String, iconId: Int, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) Color(0xFF00AFF1) else Color.DarkGray
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painterResource(id = iconId), contentDescription = label, tint = color)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = color, fontWeight = fontWeight)
    }
}
