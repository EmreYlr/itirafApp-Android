package com.itirafapp.android.data.remote.notification.dto

import com.itirafapp.android.domain.model.enums.NotificationApiEventType
import com.itirafapp.android.domain.model.enums.NotificationChannelType

data class NotificationPreferencesRequest(
    val pushEnabled: Boolean? = null,
    val emailEnabled: Boolean? = null,
    val items: List<NotificationItemRequest>? = null
)

data class NotificationItemRequest(
    val notificationType: NotificationChannelType,
    val eventType: NotificationApiEventType,
    val enabled: Boolean
)