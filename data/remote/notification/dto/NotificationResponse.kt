package com.itirafapp.android.data.remote.notification.dto

data class NotificationStatusResponse(
    val hasUnread: Boolean,
    val unreadCount: Int
)