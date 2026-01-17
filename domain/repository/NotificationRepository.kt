package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.NotificationStatus
import com.itirafapp.android.util.Resource

interface NotificationRepository {
    suspend fun fetchNotificationStatus(): Resource<NotificationStatus>
}