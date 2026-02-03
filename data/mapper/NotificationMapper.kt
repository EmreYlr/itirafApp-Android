package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.notification.dto.NotificationItemRequest
import com.itirafapp.android.data.remote.notification.dto.NotificationItemResponse
import com.itirafapp.android.data.remote.notification.dto.NotificationPreferencesRequest
import com.itirafapp.android.data.remote.notification.dto.NotificationPreferencesResponse
import com.itirafapp.android.data.remote.notification.dto.NotificationStatusResponse
import com.itirafapp.android.domain.model.NotificationEventType
import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.domain.model.NotificationItemUpdate
import com.itirafapp.android.domain.model.NotificationPreferences
import com.itirafapp.android.domain.model.NotificationPreferencesUpdate
import com.itirafapp.android.domain.model.NotificationStatus
import com.itirafapp.android.domain.model.enums.NotificationApiEventType
import com.itirafapp.android.domain.model.enums.NotificationChannelType

//RESPONSE
fun NotificationStatusResponse.toDomain(): NotificationStatus {
    return NotificationStatus(
        hasUnread = this.hasUnread,
        count = this.unreadCount
    )
}

fun NotificationPreferencesResponse.toDomain(): NotificationPreferences {
    return NotificationPreferences(
        id = this.id,
        pushEnabled = this.pushEnabled,
        emailEnabled = this.emailEnabled,
        items = this.items.mapNotNull { it.toDomain() }
    )
}

fun NotificationItemResponse.toDomain(): NotificationItem? {
    val domainEventType = mapApiEventToDomain(this.eventType) ?: return null

    return NotificationItem(
        notificationType = this.notificationType,
        eventType = domainEventType,
        enabled = this.enabled
    )
}

fun mapApiEventToDomain(apiEvent: NotificationApiEventType): NotificationEventType? {
    return when (apiEvent) {
        NotificationApiEventType.CONFESSION_PUBLISHED -> NotificationEventType.CONFESSION
        NotificationApiEventType.CONFESSION_REPLIED -> NotificationEventType.COMMENT
        NotificationApiEventType.CONFESSION_LIKED -> NotificationEventType.LIKE
        NotificationApiEventType.DM_RECEIVED -> NotificationEventType.MESSAGE
        NotificationApiEventType.DM_REQUEST_RECEIVED -> NotificationEventType.MESSAGE_REQUEST
        NotificationApiEventType.DM_REQUEST_RESPONDED -> NotificationEventType.MESSAGE_REQUEST_RESULT
        NotificationApiEventType.CONFESSION_MODERATED -> NotificationEventType.MODERATOR
    }
}

//REQUEST
fun NotificationPreferencesUpdate.toRequest(): NotificationPreferencesRequest {
    return NotificationPreferencesRequest(
        pushEnabled = this.pushEnabled,
        emailEnabled = this.emailEnabled,
        items = this.items?.map { it.toRequest() }
    )
}

fun NotificationItemUpdate.toRequest(): NotificationItemRequest {
    return NotificationItemRequest(
        notificationType = NotificationChannelType.PUSH,
        eventType = mapDomainEventToApi(this.type),
        enabled = this.enabled
    )
}

fun mapDomainEventToApi(domainEvent: NotificationEventType): NotificationApiEventType {
    return when (domainEvent) {
        NotificationEventType.CONFESSION -> NotificationApiEventType.CONFESSION_PUBLISHED
        NotificationEventType.COMMENT -> NotificationApiEventType.CONFESSION_REPLIED
        NotificationEventType.LIKE -> NotificationApiEventType.CONFESSION_LIKED
        NotificationEventType.MESSAGE -> NotificationApiEventType.DM_RECEIVED
        NotificationEventType.MESSAGE_REQUEST -> NotificationApiEventType.DM_REQUEST_RECEIVED
        NotificationEventType.MESSAGE_REQUEST_RESULT -> NotificationApiEventType.DM_REQUEST_RESPONDED
        NotificationEventType.MODERATOR -> NotificationApiEventType.CONFESSION_MODERATED
    }
}