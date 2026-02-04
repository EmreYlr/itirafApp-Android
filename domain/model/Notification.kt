package com.itirafapp.android.domain.model

data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val eventType: NotificationEventType,
    val seen: Boolean,
    val data: NotificationData,
    val createdAt: String
)

data class NotificationData(
    val roomId: String?,
    val requestId: String?,
    val senderName: String?,
    val senderId: String?,
    val messageId: String?,
    val commentId: String?,
    val status: NotificationEventStatus,
    val notificationId: String?
)

enum class NotificationEventStatus {
    ACCEPTED,
    REJECTED,
    UNKNOWN
}