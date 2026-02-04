package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.domain.model.NotificationPreferences
import com.itirafapp.android.domain.model.NotificationPreferencesUpdate
import com.itirafapp.android.domain.model.NotificationStatus
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.util.state.Resource

interface NotificationRepository {
    suspend fun fetchNotifications(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<NotificationItem>>

    suspend fun markNotificationAsSeen(notificationIds: List<String>): Resource<Unit>
    suspend fun markAllNotificationsAsSeen(): Resource<Unit>
    suspend fun deleteNotification(notificationIds: List<String>): Resource<Unit>
    suspend fun deleteAllNotification(): Resource<Unit>
    suspend fun fetchNotificationStatus(): Resource<NotificationStatus>
    suspend fun fetchNotificationPreferences(): Resource<NotificationPreferences>
    suspend fun updateNotificationPreferences(preferences: NotificationPreferencesUpdate): Resource<Unit>
}