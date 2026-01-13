package com.itirafapp.android.presentation.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.itirafapp.android.presentation.navigation.Screen
import com.itirafapp.android.presentation.screens.auth.login.LoginScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Login.route,
        route = Screen.AuthGraph.route
    ) {
        // LOGIN
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.MainGraph.route) {
                        popUpTo(Screen.AuthGraph.route) { inclusive = true }
                    }
                }
            )
        }

        // REGISTER
        composable(route = Screen.Register.route) {
            // RegisterScreen...
        }

        // FORGOT PASSWORD
        composable(route = Screen.ForgotPassword.route) {
            // ForgotPasswordScreen...
        }
    }
}