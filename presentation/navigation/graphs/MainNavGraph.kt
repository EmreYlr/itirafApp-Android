package com.itirafapp.android.presentation.navigation.graphs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.itirafapp.android.presentation.components.layout.BottomNavigation
import com.itirafapp.android.presentation.navigation.Screen
import com.itirafapp.android.presentation.screens.home.HomeScreen
import com.itirafapp.android.presentation.screens.profile.ProfileScreen
import com.itirafapp.android.presentation.screens.profile.settings.SettingsScreen

@Composable
fun MainScreen(
    onLogOut: () -> Unit
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Channel.route,
        Screen.DM.route,
        Screen.MyConfession.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            route = Screen.MainGraph.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // 1. HOME TAB
            composable(Screen.Home.route) {
                 HomeScreen(
                    onConfessionClick = { postId ->
                        navController.navigate(Screen.ItirafDetail.createRoute(postId))
                    },
                    onNotificationClick = {
                        navController.navigate(Screen.Notifications.route)
                    }
                 )
            }

            // 5. PROFILE TAB
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateToLogin = {
                        onLogOut()
                    }
                )
            }

            composable(
                route = Screen.ItirafDetail.route,
                arguments = listOf(navArgument("postId") { type = NavType.StringType })
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")
                // ItirafDetailScreen(postId = postId)
            }
        }
    }
}