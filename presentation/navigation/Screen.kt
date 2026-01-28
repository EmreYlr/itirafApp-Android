package com.itirafapp.android.presentation.navigation

import android.net.Uri.encode

sealed class Screen(val route: String) {

    // --- Root Path ---
    object Splash : Screen("splash_screen")
    object Onboarding : Screen("onboarding_screen")

    // --- Feature Graph ---
    object AuthGraph : Screen("auth_graph")
    object MainGraph : Screen("main_graph")

    // --- Auth Screens ---
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object ForgotPassword : Screen("forgot_password_screen")

    // --- Main (Tab) Screens ---
    object Home : Screen("home_screen")
    object Channel : Screen("channel_screen")
    object DM : Screen("dm_screen")
    object MyConfession : Screen("my_confession_screen")
    object Profile : Screen("profile_screen")

    // Sub Screens
    object Detail : Screen("detail/{postId}") {
        fun createRoute(postId: String) = "detail/$postId"
    }
    object ChannelDetail : Screen("channel_detail/{channelId}/{channelTitle}") {
        fun createRoute(channelId: Int, channelTitle: String): String {
            val encodedTitle = encode(channelTitle)
            return "channel_detail/$channelId/$encodedTitle"
        }
    }

    object Notifications : Screen("notifications_screen")
    object Settings : Screen("settings_screen")
}