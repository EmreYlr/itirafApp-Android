package com.itirafapp.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itirafapp.android.presentation.navigation.graphs.MainScreen
import com.itirafapp.android.presentation.navigation.graphs.authNavGraph
import com.itirafapp.android.util.SessionEvent
import androidx.hilt.navigation.compose.hiltViewModel
import com.itirafapp.android.presentation.components.common.LoginRequiredDialog

@Composable
fun RootNavigation(
    viewModel: RootViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    var showLoginAlert by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.sessionEvents.collect { event ->
            when(event) {
                is SessionEvent.LoginRequired -> {
                    showLoginAlert = true
                }
            }
        }
    }

    if (showLoginAlert) {
        LoginRequiredDialog(
            onDismiss = { showLoginAlert = false },
            onConfirm = {
                showLoginAlert = false
                navController.navigate(Screen.AuthGraph.route)
            }
        )
    }

    //val startDest = if (isLoggedIn) Screen.MainGraph.route else Screen.AuthGraph.route

    NavHost(
        navController = navController,
        startDestination = Screen.AuthGraph.route //Todo: Splash yapınca Screen.Splash.route yap
    ) {
        // SPLASH (Başlangıç)
        composable(Screen.Splash.route) {
            /*android.window.SplashScreen(
                navController = navController
                // SplashViewModel içinden gelen event ile:
                // -> Eğer Onboarding görülmediyse: navigate(Screen.Onboarding.route)
                // -> Yoksa: navigate(Screen.MainGraph.route)
            )
             */
        }

        // ONBOARDING (Placeholder)
        composable(Screen.Onboarding.route) {
            // Onboarding bittiğinde:
            navController.navigate(Screen.MainGraph.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } }
        }

        // MAIN APP
        composable(Screen.MainGraph.route) {
            MainScreen(onLogOut = {

            })
        }

        authNavGraph(navController)
    }
}