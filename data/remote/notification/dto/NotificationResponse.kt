package com.itirafapp.android.data.remote.notification.dto

import com.google.gson.annotations.SerializedName
import com.itirafapp.android.domain.model.enums.NotificationApiEventType
import com.itirafapp.android.domain.model.enums.NotificationChannelType

data class NotificationStatusResponse(
    val hasUnread: Boolean,
    val unreadCount: Int
)

data class NotificationPreferencesResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("pushEnabled")
    val pushEnabled: Boolean,

    @SerializedName("emailEnabled")
    val emailEnabled: Boolean,

    @SerializedName("items")
    val items: List<NotificationItemResponse>,

    @SerializedName("createdAt")
    val createdAt: String?,

    @SerializedName("updatedAt")
    val updatedAt: String?
)

data class NotificationItemResponse(
    @SerializedName("notificationType")
    val notificationType: NotificationChannelType,

    @SerializedName("eventType")
    val eventType: NotificationApiEventType,

    @SerializedName("enabled")
    val enabled: Boolean
)