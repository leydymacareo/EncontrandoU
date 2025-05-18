package com.leydymacareo.encontrandou.navigation


import HelpScreen
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.screens.staff.EncargadoHomeScreen
import com.leydymacareo.encontrandou.screens.user.HomeScreenUsuario
import com.leydymacareo.encontrandou.screens.login.*
import com.leydymacareo.encontrandou.viewmodel.SessionState
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel
import com.leydymacareo.encontrandou.viewmodels.SolicitudViewModel
import com.leydymacareo.encontrandou.screens.NuevaSolicitudScreen
import com.leydymacareo.encontrandou.screens.NuevoObjetoScreen
import com.leydymacareo.encontrandou.screens.staff.ConfiguracionEncargadoScreen
import com.leydymacareo.encontrandou.screens.staff.DetalleObjetoScreen
import com.leydymacareo.encontrandou.screens.staff.DetalleObjetoSeleccionableScreen
import com.leydymacareo.encontrandou.screens.staff.DetalleSolicitudEncargadoScreen
import com.leydymacareo.encontrandou.screens.staff.EncargadoProfileScreen
import com.leydymacareo.encontrandou.screens.staff.SolicitudesEncargadoScreen
import com.leydymacareo.encontrandou.screens.user.SolicitudDetailScreen
import com.leydymacareo.encontrandou.screens.user.UserProfileScreen

@Composable
fun AppNavHost(sessionViewModel: SessionViewModel = viewModel()) {
    val navController = rememberNavController()
    val sessionState = sessionViewModel.sessionState.collectAsState().value

    val solicitudViewModel: SolicitudViewModel = viewModel()

    NavHost(navController = navController, startDestination = NavRoutes.Welcome) {

        // Login & registro
        composable(NavRoutes.Welcome) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(NavRoutes.Login) },
                onRegisterClick = { navController.navigate(NavRoutes.Register) }
            )
        }

        composable(NavRoutes.Register) {
            RegisterScreen(navController, sessionViewModel) { navController.popBackStack() }
        }

        composable(NavRoutes.AccountCreated) {
            AccountCreatedScreen(navController)
        }

        composable(NavRoutes.Login) {
            LoginScreen(navController, sessionViewModel) { navController.popBackStack() }
        }

        // Home Usuario
        composable(NavRoutes.UserHome) {
            when (sessionState) {
                is SessionState.Loading -> {}
                is SessionState.LoggedOut -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(NavRoutes.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
                is SessionState.LoggedIn -> {
                    HomeScreenUsuario(navController, viewModel = solicitudViewModel, sessionViewModel = sessionViewModel)
                }
            }
        }

        // Home Encargado
        composable(NavRoutes.EncargadoHome) {
            when (sessionState) {
                is SessionState.Loading -> {}
                is SessionState.LoggedOut -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(NavRoutes.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
                is SessionState.LoggedIn -> {
                    EncargadoHomeScreen(navController = navController,
                        sessionViewModel = sessionViewModel,
                        viewModel = solicitudViewModel)
                }
            }
        }

        // Ayuda (usuario)
        composable(NavRoutes.UserHelp) {
            HelpScreen(navController)
        }

        composable(NavRoutes.UserProfile) {
            UserProfileScreen(navController = navController, sessionViewModel = sessionViewModel)
        }

        composable(NavRoutes.EncargadoProfile) {
            EncargadoProfileScreen(navController = navController, sessionViewModel = sessionViewModel)
        }


        // Solicitud Detalle
        composable(
            route = NavRoutes.DetalleSolicitud,
            arguments = listOf(
                navArgument("solicitudId") { type = NavType.StringType },
                navArgument("rol") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val solicitudId = backStackEntry.arguments?.getString("solicitudId") ?: ""
            val rol = backStackEntry.arguments?.getString("rol") ?: "usuario"

            SolicitudDetailScreen(
                solicitudId = solicitudId,
                rol = rol,
                viewModel = solicitudViewModel,
                navController = navController
            )
        }

        composable(NavRoutes.NuevaSolicitud) {
            NuevaSolicitudScreen(
                navController = navController,
                viewModel = solicitudViewModel,
                sessionViewModel = sessionViewModel
            )
        }

        composable(NavRoutes.EncargadoSolicitudes) {
            SolicitudesEncargadoScreen(navController)
        }

        composable(NavRoutes.EncargadoAjustes) {
            ConfiguracionEncargadoScreen(navController)
        }

        composable(NavRoutes.NuevoObjeto) {
            NuevoObjetoScreen(navController, solicitudViewModel, sessionViewModel)
        }
        composable(
            route = NavRoutes.DetalleObjeto,
            arguments = listOf(navArgument("objetoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val objetoId = backStackEntry.arguments?.getString("objetoId") ?: ""
            DetalleObjetoScreen(
                objetoId = objetoId,
                viewModel = solicitudViewModel,
                navController = navController
            )
        }
        composable(
            route = NavRoutes.DetalleSolicitudEncargado,
            arguments = listOf(navArgument("solicitudId") { type = NavType.StringType })
        ) { backStackEntry ->
            val solicitudId = backStackEntry.arguments?.getString("solicitudId") ?: ""
            DetalleSolicitudEncargadoScreen(
                solicitudId = solicitudId,
                viewModel = solicitudViewModel,
                navController = navController
            )
        }

        composable(
            route = NavRoutes.DetalleObjetoSeleccionable,
            arguments = listOf(
                navArgument("solicitudId") { type = NavType.StringType },
                navArgument("objetoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val solicitudId = backStackEntry.arguments?.getString("solicitudId") ?: ""
            val objetoId = backStackEntry.arguments?.getString("objetoId") ?: ""
            DetalleObjetoSeleccionableScreen(
                solicitudId = solicitudId,
                objetoId = objetoId,
                navController = navController,
                viewModel = solicitudViewModel
            )

        }

    }
}

