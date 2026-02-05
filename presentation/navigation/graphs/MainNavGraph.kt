package com.itirafapp.android.presentation.navigation.graphs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.presentation.components.layout.BottomNavigation
import com.itirafapp.android.presentation.components.layout.BottomSheetType
import com.itirafapp.android.presentation.navigation.Screen
import com.itirafapp.android.presentation.screens.channel.ChannelScreen
import com.itirafapp.android.presentation.screens.channel.channel_detail.ChannelDetailScreen
import com.itirafapp.android.presentation.screens.home.HomeScreen
import com.itirafapp.android.presentation.screens.home.detail.DetailScreen
import com.itirafapp.android.presentation.screens.home.dm_request.DMRequestScreen
import com.itirafapp.android.presentation.screens.home.notification.NotificationScreen
import com.itirafapp.android.presentation.screens.my_confession.MyConfessionScreen
import com.itirafapp.android.presentation.screens.my_confession.my_confession_detail.MyConfessionDetailScreen
import com.itirafapp.android.presentation.screens.my_confession.my_confession_edit.MyConfessionEditConfessionScreen
import com.itirafapp.android.presentation.screens.post.PostScreen
import com.itirafapp.android.presentation.screens.profile.ProfileScreen
import com.itirafapp.android.presentation.screens.profile.follow_channel.FollowChannelScreen
import com.itirafapp.android.presentation.screens.profile.settings.SettingsScreen
import com.itirafapp.android.presentation.screens.profile.settings.edit_profile.EditProfileScreen
import com.itirafapp.android.presentation.screens.profile.settings.notification.NotificationPreferencesScreen
import com.itirafapp.android.presentation.screens.profile.social.SocialScreen
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
        Screen.Message.route,
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
            composable(Screen.Home.route) {
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
                        currentSheet = BottomSheetType.AddPost(-1)
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

            // 3. MESSAGE TAB
            composable(Screen.Message.route) {

            }

            // 4. MY CONFESSION TAB
            composable(Screen.MyConfession.route) {
                MyConfessionScreen(
                    onItemClick = { confessionData ->

                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "selected_confession",
                            value = confessionData
                        )

                        navController.navigate(Screen.MyConfessionDetail.route)
                    },

                    onEditClick = { confessionData ->
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "edit_confession",
                            value = confessionData
                        )

                        navController.navigate(Screen.MyConfessionEditConfession.route)
                    }
                )
            }

            // 5. PROFILE TAB
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    },

                    onNavigateToFollowChannel = {
                        navController.navigate(Screen.FollowChannel.route)
                    },

                    onNavigateToSocial = { link ->
                        if (link != null) {
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("social_data", link)
                        } else {
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.remove<Link>("social_data")
                        }

                        navController.navigate(Screen.Social.route)
                    }
                )
            }

            // SETTINGS SCREEN
            animatedComposable(Screen.Settings.route) {
                SettingsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNavigateToLogin = {
                        onLogOut()
                    },
                    onNavigateToEdit = {
                        navController.navigate(Screen.EditProfile.route)
                    },
                    onNavigateToNotification = {
                        navController.navigate(Screen.NotificationPreferences.route)
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

            // MY CONFESSION DETAIL SCREEN
            animatedComposable(Screen.MyConfessionDetail.route) {
                val confessionData = remember {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<MyConfessionData>("selected_confession")
                }

                if (confessionData != null) {
                    MyConfessionDetailScreen(
                        data = confessionData,
                        onBackClick = { navController.popBackStack() },
                        onOpenReport = { target ->
                            currentSheet = BottomSheetType.Report(target)
                        },
                        onEditClick = { data ->
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "edit_confession",
                                value = data
                            )

                            navController.navigate(Screen.MyConfessionEditConfession.route)
                        }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
            }

            // EDIT CONFESSION SCREEN
            animatedComposable(Screen.MyConfessionEditConfession.route) {
                val editData = remember {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<MyConfessionData>("edit_confession")
                }

                if (editData != null) {
                    MyConfessionEditConfessionScreen(
                        data = editData,
                        onBackClick = { navController.popBackStack() },
                        onSuccess = {
                            navController.popBackStack(Screen.MyConfession.route, false)
                        }
                    )
                } else {
                    LaunchedEffect(Unit) { navController.popBackStack() }
                }
            }

            // SOCIAL SCREEN
            animatedComposable(Screen.Social.route) {
                val socialData = remember {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<Link>("social_data")
                }

                SocialScreen(
                    data = socialData,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            //FOLLOW CHANNEL SCREEN
            animatedComposable(Screen.FollowChannel.route) {
                FollowChannelScreen(
                    onBackClick = { navController.popBackStack() },
                    onChannelClick = { channelId, channelTitle ->
                        navController.navigate(
                            Screen.ChannelDetail.createRoute(channelId, channelTitle)
                        )
                    }
                )
            }

            //EDIT PROFILE SCREEN
            animatedComposable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onDeleteClick = {
                        onLogOut()
                    }
                )
            }

            //NOTIFICATION PREFERENCES SCREEN
            animatedComposable(Screen.NotificationPreferences.route) {
                NotificationPreferencesScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            //NOTIFICATION SCREEN
            animatedComposable(Screen.Notifications.route) {
                NotificationScreen(
                    onBackClick = { navController.popBackStack() },
                    onNavigate = { route ->
                        if (route == Screen.Home.route) {
                            navController.popBackStack(Screen.Home.route, inclusive = false)
                        } else {
                            navController.navigate(route)
                        }
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
                    if (type.channelId != -1) {
                        PostScreen(
                            channelId = type.channelId,
                            onDismiss = { closeSheet() }
                        )
                    } else {
                        PostScreen(
                            channelId = -1,
                            onDismiss = { closeSheet() }
                        )
                    }
                }

                else -> {}
            }
        }
    }
}
