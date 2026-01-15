package com.itirafapp.android.presentation.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.itirafapp.android.presentation.navigation.Screen
import com.itirafapp.android.presentation.navigation.animatedComposable
import com.itirafapp.android.presentation.screens.auth.login.LoginScreen
import com.itirafapp.android.presentation.screens.auth.passwordreset.PasswordResetScreen
import com.itirafapp.android.presentation.screens.auth.register.RegisterScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Login.route,
        route = Screen.AuthGraph.route
    ) {
        // LOGIN
        animatedComposable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToPasswordReset = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.MainGraph.route) {
                        popUpTo(Screen.AuthGraph.route) { inclusive = true }
                    }
                }
            )
        }

        // REGISTER
        animatedComposable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // FORGOT PASSWORD
        animatedComposable(route = Screen.ForgotPassword.route) {
            PasswordResetScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}