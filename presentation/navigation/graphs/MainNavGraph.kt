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
import com.itirafapp.android.presentation.navigation.Screen

@Composable
fun MainScreen() {
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
                //ItirafBottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        // Bottom Bar navigasyon yapısı
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            route = Screen.MainGraph.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. HOME TAB
            composable(Screen.Home.route) {
                // HomeScreen(
                //     onItirafClick = { postId ->
                //         navController.navigate(Screen.ItirafDetail.createRoute(postId))
                //     }
                // )
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