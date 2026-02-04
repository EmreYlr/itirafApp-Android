package com.itirafapp.android.domain.model

import com.itirafapp.android.domain.model.enums.NotificationChannelType

data class NotificationPreferences(
    val id: String,
    val pushEnabled: Boolean,
    val emailEnabled: Boolean,
    val items: List<NotificationPreferencesItem>
)

data class NotificationPreferencesItem(
    val notificationType: NotificationChannelType,
    val eventType: NotificationEventType,
    val enabled: Boolean
)

enum class NotificationEventType {
    CONFESSION,
    MESSAGE,
    COMMENT,
    LIKE,
    MESSAGE_REQUEST,
    MESSAGE_REQUEST_RESULT,
    MODERATOR,
    ADMIN_REVIEW_REQUIRED
}

data class NotificationPreferencesUpdate(
    val pushEnabled: Boolean? = null,
    val emailEnabled: Boolean? = null,
    val items: List<NotificationItemUpdate>? = null
)

data class NotificationItemUpdate(
    val type: NotificationEventType,
    val enabled: Boolean
)