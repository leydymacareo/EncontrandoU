// AppNavHost.kt
package com.leydymacareo.encontrandou.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.screens.staff.EncargadoHomeScreen
import com.leydymacareo.encontrandou.screens.home.HomeScreen
import com.leydymacareo.encontrandou.screens.login.*
import com.leydymacareo.encontrandou.viewmodel.SessionState
import com.leydymacareo.encontrandou.viewmodel.SessionViewModel

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
                    HomeScreen()
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
    }
}
