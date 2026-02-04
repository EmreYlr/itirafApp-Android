package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.notification.dto.NotificationDataResponse
import com.itirafapp.android.data.remote.notification.dto.NotificationItemResponse
import com.itirafapp.android.data.remote.notification.dto.NotificationResponse
import com.itirafapp.android.domain.model.NotificationData
import com.itirafapp.android.domain.model.NotificationEventStatus
import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.domain.model.PaginatedResult

fun NotificationResponse.toDomain(): PaginatedResult<NotificationItem> {
    return PaginatedResult(
        items = this.data.mapNotNull { it.toDomain() },

        page = this.page,
        totalPages = this.totalPages,

        hasNextPage = this.page < this.totalPages
    )
}

fun NotificationItemResponse.toDomain(): NotificationItem? {
    val domainEventType = mapApiEventToDomain(this.eventType) ?: return null

    return NotificationItem(
        id = this.id,
        title = this.title,
        body = this.body,
        eventType = domainEventType,
        seen = this.seen,
        data = this.data.toDomain(),
        createdAt = this.createdAt
    )
}

fun NotificationDataResponse.toDomain(): NotificationData {
    return NotificationData(
        roomId = this.roomId,
        requestId = this.requestId,
        senderName = this.senderName,
        senderId = this.senderId,
        messageId = this.messageId,
        commentId = this.commentId,
        status = mapStatusToDomain(this.status),
        notificationId = this.notificationId
    )
}

private fun mapStatusToDomain(status: String?): NotificationEventStatus {
    return when (status) {
        "ACCEPTED" -> NotificationEventStatus.ACCEPTED
        "REJECTED" -> NotificationEventStatus.REJECTED
        else -> NotificationEventStatus.UNKNOWN
    }
}