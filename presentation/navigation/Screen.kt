package com.itirafapp.android.presentation.navigation

import android.net.Uri.encode

sealed class Screen(val route: String) {

    // --- Root Path ---
    object Splash : Screen("splash_screen")
    object Onboarding : Screen("onboarding_screen")
    object Terms : Screen("terms_screen")

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
    object Message : Screen("message?tabIndex={tabIndex}") {
        fun createRoute(tabIndex: Int = 0) = "message?tabIndex=$tabIndex"
    }
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

    object Chat : Screen("chat/{roomId}/{roomTitle}") {
        fun createRoute(roomId: String, roomTitle: String): String {
            val encodedTitle = encode(roomTitle)
            return "chat/$roomId/$encodedTitle"
        }
    }

    object MyConfessionDetail : Screen("my_confession_detail")
    object MyConfessionEditConfession : Screen("my_confession_edit")
    object Social : Screen("social")
    object FollowChannel : Screen("follow_channel")
    object EditProfile : Screen("edit_profile")
    object NotificationPreferences : Screen("notification_preferences")
    object SentMessage : Screen("sent_message")
    object SentMessageDetail : Screen("sent_message_detail")
    object InboxDetail : Screen("inbox_detail")
    object Moderation : Screen("moderation")

    object Notifications : Screen("notifications_screen")
    object Settings : Screen("settings_screen")
}