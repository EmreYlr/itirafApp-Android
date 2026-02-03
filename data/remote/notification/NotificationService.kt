package com.itirafapp.android.data.remote.notification

import com.itirafapp.android.data.remote.notification.dto.NotificationPreferencesRequest
import com.itirafapp.android.data.remote.notification.dto.NotificationPreferencesResponse
import com.itirafapp.android.data.remote.notification.dto.NotificationStatusResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT

interface NotificationService {
    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("notifications/status")
    suspend fun fetchNotificationStatus(): NotificationStatusResponse

    @GET("users/me/notification-preferences")
    suspend fun fetchNotificationPreferences(): NotificationPreferencesResponse


    @PUT("users/me/notification-preferences")
    suspend fun updateNotificationPreferences(
        @Body request: NotificationPreferencesRequest
    ): Unit
}