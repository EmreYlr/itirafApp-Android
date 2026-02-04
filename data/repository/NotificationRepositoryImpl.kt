package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.mapper.toRequest
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.data.remote.notification.NotificationService
import com.itirafapp.android.data.remote.notification.dto.NotificationDeleteRequest
import com.itirafapp.android.data.remote.notification.dto.NotificationSeenRequest
import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.domain.model.NotificationPreferences
import com.itirafapp.android.domain.model.NotificationPreferencesUpdate
import com.itirafapp.android.domain.model.NotificationStatus
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationService
) : NotificationRepository {

    override suspend fun fetchNotifications(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<NotificationItem>> {
        return safeApiCall {
            val response = api.fetchNotifications(page, limit)

            response.toDomain()
        }
    }

    override suspend fun markNotificationAsSeen(notificationIds: List<String>): Resource<Unit> {
        if (notificationIds.isEmpty()) return Resource.Success(Unit)

        return safeApiCall {
            val request = NotificationSeenRequest(notificationIds)
            api.updateNotificationSeen(request)
        }
    }

    override suspend fun markAllNotificationsAsSeen(): Resource<Unit> {
        return safeApiCall {
            api.updateNotificationSeenAll()
        }
    }

    override suspend fun deleteNotification(notificationIds: List<String>): Resource<Unit> {
        if (notificationIds.isEmpty()) return Resource.Success(Unit)

        return safeApiCall {
            val request = NotificationDeleteRequest(notificationIds)
            api.deleteNotification(request)
        }
    }

    override suspend fun deleteAllNotification(): Resource<Unit> {
        return safeApiCall {
            api.deleteNotificationAll()
        }
    }

    override suspend fun fetchNotificationStatus(): Resource<NotificationStatus> {
        return safeApiCall {
            val response = api.fetchNotificationStatus()

            val notification = response.toDomain()

            notification
        }
    }

    override suspend fun fetchNotificationPreferences(): Resource<NotificationPreferences> {
        return safeApiCall {
            val response = api.fetchNotificationPreferences()

            val domainData = response.toDomain()

            domainData
        }
    }

    override suspend fun updateNotificationPreferences(preferences: NotificationPreferencesUpdate): Resource<Unit> {
        return safeApiCall {
            val request = preferences.toRequest()

            api.updateNotificationPreferences(request)
        }
    }
}