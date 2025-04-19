package com.leydymacareo.encontrandou.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.screens.login.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.Welcome) {
        composable(NavRoutes.Welcome) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(NavRoutes.Login) },
                onRegisterClick = { navController.navigate(NavRoutes.Register) }
            )
        }
        composable(NavRoutes.Login) {
            LoginSreen(
                onLoginSuccess = { navController.navigate(NavRoutes.Verification) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.Register) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(NavRoutes.Verification) }
            )
        }
        composable(NavRoutes.Verification) {
            VerificationScreen(
                onBack = { navController.popBackStack() },
                onVerified = { navController.navigate(NavRoutes.AccountCreated) }
            )
        }
        composable(NavRoutes.AccountCreated) {
            AccountCreatedScreen(
                onGoHome = {
                    navController.navigate(NavRoutes.Welcome) {
                        popUpTo(0) // limpia el backstack completo
                    }
                }
            )
        }
    }
}
