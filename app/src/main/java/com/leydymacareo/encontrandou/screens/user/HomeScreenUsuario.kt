package com.leydymacareo.encontrandou.screens.user
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.viewmodel.SessionState
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel


@Composable
fun HomeScreenUsuario(
    navController: NavController,
    viewModel: SolicitudViewModel = viewModel(),
    sessionViewModel: SessionViewModel
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val correoUsuario = (sessionState as? SessionState.LoggedIn)?.email ?: ""

    // Cargar solicitudes de este usuario
    LaunchedEffect(sessionState) {
        viewModel.cargarSolicitudesDeUsuario(correoUsuario)
    }

    val solicitudes by viewModel.solicitudes.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
                    .padding(top = 40.dp, bottom = 20.dp, start = 20.dp),
                horizontalAlignment = Alignment.Start
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
                onClick = {
                    navController.navigate(NavRoutes.NuevaSolicitud)
                },
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
                    NavBarItem(
                        icon = painterResource(id = R.drawable.home),
                        label = "Inicio",
                        isSelected = currentRoute == NavRoutes.UserHome,
                        onClick = {
                            if (currentRoute != NavRoutes.UserHome) {
                                navController.navigate(NavRoutes.UserHome)
                            }
                        }
                    )

                    NavBarItem(
                        icon = painterResource(id = R.drawable.help),
                        label = "Ayuda",
                        isSelected = currentRoute == NavRoutes.UserHelp,
                        onClick = {
                            if (currentRoute != NavRoutes.UserHelp) {
                                navController.navigate(NavRoutes.UserHelp)
                            }
                        }
                    )

                    NavBarItem(
                        icon = painterResource(id = R.drawable.person),
                        label = "Perfil",
                        isSelected = currentRoute == NavRoutes.UserProfile,
                        onClick = {
                            if (currentRoute != NavRoutes.UserProfile) {
                                navController.navigate(NavRoutes.UserProfile)
                            }
                        }
                    )
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

            if (solicitudes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no has creado ninguna solicitud",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            solicitudes.forEach { solicitud ->
                val estado = solicitud.estado
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("detalle_solicitud/${solicitud.id}/usuario")
                        },
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
                            Text(text = solicitud.nombreObjeto, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(text = solicitud.fecha, color = Color.Black, fontSize = 13.sp)
                            Text(
                                text = solicitud.estado,
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
fun NavBarItem(
    icon: Painter,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) Color(0xFF00AFF1) else Color.DarkGray
    val textColor = if (isSelected) Color(0xFF00AFF1) else Color.Black
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = icon, contentDescription = label, tint = color)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = textColor, fontWeight = fontWeight)
    }
}
