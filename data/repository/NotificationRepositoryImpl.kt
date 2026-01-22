package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.data.remote.notification.NotificationService
import com.itirafapp.android.domain.model.NotificationStatus
import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationService
): NotificationRepository {

    override suspend fun fetchNotificationStatus(): Resource<NotificationStatus> {
        return safeApiCall {
            val response = api.fetchNotificationStatus()

            val notification = response.toDomain()

            notification
        }
    }
}