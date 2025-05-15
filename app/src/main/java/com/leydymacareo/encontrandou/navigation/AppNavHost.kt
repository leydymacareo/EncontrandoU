package com.leydymacareo.encontrandou.navigation

import DetalleSolicitudScreen
import HelpScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.screens.staff.EncargadoHomeScreen
import com.leydymacareo.encontrandou.screens.home.HomeScreenUsuario
import com.leydymacareo.encontrandou.screens.login.*
import com.leydymacareo.encontrandou.viewmodel.SessionState
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.leydymacareo.encontrandou.screens.NuevaSolicitudScreen
import com.leydymacareo.encontrandou.screens.profile.ProfileScreen


@Composable
fun AppNavHost(sessionViewModel: SessionViewModel = viewModel()) {
    val navController = rememberNavController()
    val sessionState = sessionViewModel.sessionState.collectAsState().value

    NavHost(navController = navController, startDestination = NavRoutes.Welcome) {

        composable(NavRoutes.Welcome) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(NavRoutes.Login) },
                onRegisterClick = { navController.navigate(NavRoutes.Register) }
            )
        }

        composable(NavRoutes.Register) {
            RegisterScreen(
                navController = navController,
                sessionViewModel = sessionViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.AccountCreated) {
            AccountCreatedScreen(navController)
        }

        composable(NavRoutes.Login) {
            LoginScreen(
                navController = navController,
                sessionViewModel = sessionViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.UserHome) {
            when (sessionState) {
                is SessionState.Loading -> {
                    // Aquí podrías mostrar un indicador de carga
                }
                is SessionState.LoggedOut -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(NavRoutes.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
                is SessionState.LoggedIn -> {
                    HomeScreenUsuario(navController)
                }
            }
        }

        composable(NavRoutes.EncargadoHome) {
            when (sessionState) {
                is SessionState.Loading -> {
                    // Aquí podrías mostrar un indicador de carga
                }
                is SessionState.LoggedOut -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(NavRoutes.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
                is SessionState.LoggedIn -> {
                    EncargadoHomeScreen(navController = navController,
                        sessionViewModel = sessionViewModel)

                }
            }
        }
        composable(NavRoutes.UserHelp) {
            HelpScreen(navController)
        }
        composable(NavRoutes.UserProfile) {
            ProfileScreen(navController)
        }
        composable(
            route = NavRoutes.DetalleSolicitud,
            arguments = listOf(
                navArgument("solicitudId") { type = NavType.StringType },
                navArgument("rol") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val solicitudId = backStackEntry.arguments?.getString("solicitudId") ?: ""
            val rol = backStackEntry.arguments?.getString("rol") ?: "usuario"

            // Pasa ambos argumentos al Composable
            DetalleSolicitudScreen(rol = rol, solicitudId = solicitudId)
        }

        composable(NavRoutes.NuevaSolicitud) {
            NuevaSolicitudScreen(navController)
        }


    }
}
