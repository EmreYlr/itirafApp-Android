package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.NotificationPreferences
import com.itirafapp.android.domain.model.NotificationPreferencesUpdate
import com.itirafapp.android.domain.model.NotificationStatus
import com.itirafapp.android.util.state.Resource

interface NotificationRepository {
    suspend fun fetchNotificationStatus(): Resource<NotificationStatus>
    suspend fun fetchNotificationPreferences(): Resource<NotificationPreferences>
    suspend fun updateNotificationPreferences(preferences: NotificationPreferencesUpdate): Resource<Unit>
}