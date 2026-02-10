package com.itirafapp.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itirafapp.android.presentation.main.MainViewModel
import com.itirafapp.android.presentation.navigation.components.SessionDialogHandler
import com.itirafapp.android.presentation.navigation.graphs.MainScreen
import com.itirafapp.android.presentation.navigation.graphs.authNavGraph
import com.itirafapp.android.presentation.screens.onboarding.OnboardingScreen
import com.itirafapp.android.presentation.screens.splash.SplashScreen
import com.itirafapp.android.presentation.screens.terms.TermsScreen
import com.itirafapp.android.util.manager.SessionEvent

@Composable
fun RootNavigation(
    viewModel: RootViewModel = hiltViewModel(),
    mainViewModel: MainViewModel
) {
    val navController = rememberNavController()
    var activeSessionEvent by remember { mutableStateOf<SessionEvent?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sessionEvents.collect { event ->
            activeSessionEvent = event
        }
    }

    val handleSessionExpiredAndNavigate = {
        viewModel.onSessionExpiredConfirmed {
            activeSessionEvent = null
            navController.navigate(Screen.AuthGraph.route) {
                popUpTo(0) { inclusive = true }
            }
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
        onConfirm = {
            when (activeSessionEvent) {
                is SessionEvent.SessionExpired -> handleSessionExpiredAndNavigate()
                else -> {
                    activeSessionEvent = null
                    navController.navigate(Screen.AuthGraph.route)
                }
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // SPLASH
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        // ONBOARDING
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onboardingCompleted = {
                    navController.navigate(Screen.Terms.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // TERMS
        composable(Screen.Terms.route) {
            TermsScreen(
                onTermsComplete = {
                    navController.navigate(Screen.MainGraph.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onAuthRedirect = {
                    navigateToAuth()
                }
            )
        }

        // MAIN APP
        composable(Screen.MainGraph.route) {
            MainScreen(onLogOut = navigateToAuth, mainViewModel)
        }

        authNavGraph(navController)
    }
}