package com.leydymacareo.encontrandou.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.R
import com.leydymacareo.encontrandou.components.ProfileContent
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.leydymacareo.encontrandou.viewmodel.SessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(navController: NavController, sessionViewModel: SessionViewModel = viewModel()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
                    text = "Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
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
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavBarItem(
                        icon = painterResource(id = R.drawable.home),
                        label = "Inicio",
                        isSelected = currentRoute == NavRoutes.UserHome
                    ) {
                        navController.navigate(NavRoutes.UserHome)
                    }

                    NavBarItem(
                        icon = painterResource(id = R.drawable.help),
                        label = "Ayuda",
                        isSelected = currentRoute == NavRoutes.UserHelp
                    ) {
                        navController.navigate(NavRoutes.UserHelp)
                    }

                    NavBarItem(
                        icon = painterResource(id = R.drawable.person),
                        label = "Perfil",
                        isSelected = currentRoute == NavRoutes.UserProfile
                    ) {
                        navController.navigate(NavRoutes.UserProfile)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val sessionState by sessionViewModel.sessionState.collectAsState()

            val nombre = (sessionState as? SessionState.LoggedIn)?.name ?: "Nombre no disponible"
            val correo = (sessionState as? SessionState.LoggedIn)?.email ?: "Correo no disponible"
            val rol = (sessionState as? SessionState.LoggedIn)?.role?.replaceFirstChar { it.uppercase() } ?: "Desconocido"

            ProfileContent(
                nombre = nombre,
                correo = correo,
                rol = rol,
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
