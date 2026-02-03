package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.mapper.toRequest
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.data.remote.notification.NotificationService
import com.itirafapp.android.domain.model.NotificationPreferences
import com.itirafapp.android.domain.model.NotificationPreferencesUpdate
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

    override suspend fun fetchNotificationPreferences(): Resource<NotificationPreferences> {
        return try {
            val response = api.fetchNotificationPreferences()
            val domainData = response.toDomain()
            Resource.Success(domainData)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Hata oluştu")
        }
    }

    override suspend fun updateNotificationPreferences(preferences: NotificationPreferencesUpdate): Resource<Unit> {
        return try {
            val request = preferences.toRequest()
            api.updateNotificationPreferences(request)
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Ayarlar güncellenirken bir hata oluştu.")
        }
    }
}