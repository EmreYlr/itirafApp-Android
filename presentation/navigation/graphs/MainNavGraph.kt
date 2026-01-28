package com.itirafapp.android.presentation.navigation.graphs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.itirafapp.android.presentation.components.layout.BottomNavigation
import com.itirafapp.android.presentation.components.layout.BottomSheetType
import com.itirafapp.android.presentation.navigation.Screen
import com.itirafapp.android.presentation.screens.channel.ChannelScreen
import com.itirafapp.android.presentation.screens.channel.channel_detail.ChannelDetailScreen
import com.itirafapp.android.presentation.screens.home.HomeScreen
import com.itirafapp.android.presentation.screens.home.detail.DetailScreen
import com.itirafapp.android.presentation.screens.home.dm_request.DMRequestScreen
import com.itirafapp.android.presentation.screens.profile.ProfileScreen
import com.itirafapp.android.presentation.screens.profile.settings.SettingsScreen
import com.itirafapp.android.presentation.screens.report.ReportScreen
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.animatedComposable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogOut: () -> Unit
) {
    val navController = rememberNavController()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var currentSheet by remember { mutableStateOf<BottomSheetType>(BottomSheetType.None) }
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Channel.route,
        Screen.DM.route,
        Screen.MyConfession.route,
        Screen.Profile.route
    )

    fun closeSheet() {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                currentSheet = BottomSheetType.None
            }
        }
    }

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
            animatedComposable(Screen.Home.route) {
                HomeScreen(
                    onConfessionClick = { postId ->
                        navController.navigate(Screen.Detail.createRoute(postId))
                    },
                    onNotificationClick = {
                        navController.navigate(Screen.Notifications.route)
                    },
                    onOpenDM = { targetId ->
                        currentSheet = BottomSheetType.DMRequest(targetId)
                    },
                    onPostConfessionClick = {
                        currentSheet = BottomSheetType.AddPost()
                    },
                    onChannelClick = { channelId, channelTitle ->
                        navController.navigate(
                            Screen.ChannelDetail.createRoute(channelId, channelTitle)
                        )
                    }
                )
            }

            // 2. CHANNEL TAB
            composable(Screen.Channel.route) {
                ChannelScreen(
                    onChannelClick = { channelId, channelTitle ->
                        navController.navigate(
                            Screen.ChannelDetail.createRoute(channelId, channelTitle)
                        )
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

            // SETTINGS SCREEN
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateToLogin = {
                        onLogOut()
                    }
                )
            }

            // DETAIL SCREEN
            animatedComposable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("postId") { type = NavType.StringType })
            ) {
                DetailScreen(
                    onNavigationBack = { navController.navigateUp() },
                    onOpenDM = { targetId ->
                        currentSheet = BottomSheetType.DMRequest(targetId)
                    },
                    onOpenReport = { target ->
                        currentSheet = BottomSheetType.Report(target)
                    }
                )
            }

            // CHANNEL DETAIL SCREEN
            animatedComposable(
                route = Screen.ChannelDetail.route,
                arguments = listOf(
                    navArgument("channelId") { type = NavType.IntType },
                    navArgument("channelTitle") { type = NavType.StringType }
                )
            ) {
                ChannelDetailScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onConfessionClick = { postId ->
                        navController.navigate(Screen.Detail.createRoute(postId))
                    },
                    onAddPostClick = { channelId ->
                        currentSheet = BottomSheetType.AddPost(channelId)
                    }
                )
            }
        }
    }

    if (currentSheet !is BottomSheetType.None) {
        ModalBottomSheet(
            onDismissRequest = {
                currentSheet = BottomSheetType.None
            },
            sheetState = sheetState,
            containerColor = ItirafTheme.colors.backgroundApp
        ) {
            when (val type = currentSheet) {
                is BottomSheetType.DMRequest -> {
                    DMRequestScreen(
                        targetId = type.targetId,
                        onDismiss = { closeSheet() }
                    )
                }

                is BottomSheetType.Report -> {
                    ReportScreen(
                        target = type.target,
                        onDismiss = { closeSheet() }
                    )
                }

                is BottomSheetType.AddPost -> {
//                    if (type.channelId != null) {
//                        PostConfessionScreen(
//                            channelId = type.channelId,
//                            onDismiss = { closeSheet() }
//                        )
//                    } else {
//                        PostConfessionScreen(
//                            onDismiss = { closeSheet() }
//                        )
//                    }
                }

                else -> {}
            }
        }
    }
}
