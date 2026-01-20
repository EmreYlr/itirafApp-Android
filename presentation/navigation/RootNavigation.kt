package com.itirafapp.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itirafapp.android.presentation.navigation.components.SessionDialogHandler
import com.itirafapp.android.presentation.navigation.graphs.MainScreen
import com.itirafapp.android.presentation.navigation.graphs.authNavGraph
import com.itirafapp.android.presentation.screens.splash.SplashScreen
import com.itirafapp.android.util.SessionEvent

@Composable
fun RootNavigation(
    viewModel: RootViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    var activeSessionEvent by remember { mutableStateOf<SessionEvent?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sessionEvents.collect { event ->
            activeSessionEvent = event
        }
    }

    val navigateToAuth = {
        navController.navigate(Screen.AuthGraph.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    SessionDialogHandler(
        event = activeSessionEvent,
        onDismiss = { activeSessionEvent = null },
        onConfirm = navigateToAuth
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // SPLASH
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        // ONBOARDING (Placeholder)
        composable(Screen.Onboarding.route) {
            // Onboarding bittiğinde:
            navController.navigate(Screen.MainGraph.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } }
        }

        // MAIN APP
        composable(Screen.MainGraph.route) {
            MainScreen(onLogOut = navigateToAuth)
        }

        authNavGraph(navController)
    }
}