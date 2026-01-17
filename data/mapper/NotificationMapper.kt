package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.notification.dto.NotificationStatusResponse
import com.itirafapp.android.domain.model.NotificationStatus

fun NotificationStatusResponse.toDomain(): NotificationStatus {
    return NotificationStatus(
        hasUnread = this.hasUnread,
        count = this.unreadCount
    )
}