package com.itirafapp.android.data.remote.notification.dto

import com.google.gson.annotations.SerializedName
import com.itirafapp.android.domain.model.enums.NotificationApiEventType
import com.itirafapp.android.domain.model.enums.NotificationChannelType

data class NotificationResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("totalRows")
    val totalRows: Int,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("data")
    val data: List<NotificationItemResponse>
)

data class NotificationItemResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("eventType")
    val eventType: NotificationApiEventType,
    @SerializedName("seen")
    val seen: Boolean,
    @SerializedName("data")
    val data: NotificationDataResponse,
    @SerializedName("createdAt")
    val createdAt: String
)

data class NotificationDataResponse(
    @SerializedName("roomId")
    val roomId: String?,
    @SerializedName("requestId")
    val requestId: String?,
    @SerializedName("senderName")
    val senderName: String?,
    @SerializedName("senderId")
    val senderId: String?,
    @SerializedName("messageId")
    val messageId: String?,
    @SerializedName("commentId")
    val commentId: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("notificationId")
    val notificationId: String?
)

//STATUS
data class NotificationStatusResponse(
    val hasUnread: Boolean,
    val unreadCount: Int
)


//PREFERENCES
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
    val items: List<NotificationPreferencesItemResponse>,

    @SerializedName("createdAt")
    val createdAt: String?,

    @SerializedName("updatedAt")
    val updatedAt: String?
)

data class NotificationPreferencesItemResponse(
    @SerializedName("notificationType")
    val notificationType: NotificationChannelType,

    @SerializedName("eventType")
    val eventType: NotificationApiEventType,

    @SerializedName("enabled")
    val enabled: Boolean
)