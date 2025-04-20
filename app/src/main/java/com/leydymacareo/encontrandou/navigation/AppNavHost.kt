package com.leydymacareo.encontrandou.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leydymacareo.encontrandou.NavRoutes
import com.leydymacareo.encontrandou.screens.login.*
import com.leydymacareo.encontrandou.screens.user.HomeScreen

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

        composable(NavRoutes.Register) {
            RegisterScreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }


        composable(NavRoutes.AccountCreated) {
            AccountCreatedScreen(navController)
        }

        composable(NavRoutes.Login) {
            LoginSreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.VerifyEmail) {
            VerificationScreen(
                onBack = { navController.popBackStack() },
                onVerified = {
                    navController.navigate(NavRoutes.Home) {
                        popUpTo(NavRoutes.Login) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Home) {
            HomeScreen()
        }
    }
}
