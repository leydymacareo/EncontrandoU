package com.leydymacareo.encontrandou.screens.staff

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.components.ProfileContent
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncargadoProfileScreen(navController: NavController, sessionViewModel: SessionViewModel = viewModel()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Perfil") })
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
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EncargadoNavItem(
                        label = "Inventario",
                        iconId = R.drawable.inventory,
                        isSelected = currentRoute == NavRoutes.EncargadoHome
                    ) {
                        navController.navigate(NavRoutes.EncargadoHome)
                    }

                    EncargadoNavItem(
                        label = "Solicitudes",
                        iconId = R.drawable.list,
                        isSelected = currentRoute == NavRoutes.EncargadoSolicitudes
                    ) {
                        navController.navigate(NavRoutes.EncargadoSolicitudes)
                    }

                    EncargadoNavItem(
                        label = "Perfil",
                        iconId = R.drawable.person,
                        isSelected = currentRoute == NavRoutes.EncargadoProfile
                    ) {
                        navController.navigate(NavRoutes.EncargadoProfile)
                    }

                    EncargadoNavItem(
                        label = "Ajustes",
                        iconId = R.drawable.settings,
                        isSelected = currentRoute == NavRoutes.EncargadoAjustes
                    ) {
                        navController.navigate(NavRoutes.EncargadoAjustes)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ProfileContent(
                nombre = "Laura Encargada",
                correo = "lencargada@unab.edu.co",
                rol = "Encargado",
                onLogout = {
                    sessionViewModel.logout()
                    navController.navigate(NavRoutes.Welcome) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
