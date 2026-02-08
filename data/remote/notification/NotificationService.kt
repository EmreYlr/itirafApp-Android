package com.itirafapp.android.data.remote.notification

import com.itirafapp.android.data.remote.notification.dto.NotificationDeleteRequest
import com.itirafapp.android.data.remote.notification.dto.NotificationPreferencesRequest
import com.itirafapp.android.data.remote.notification.dto.NotificationPreferencesResponse
import com.itirafapp.android.data.remote.notification.dto.NotificationResponse
import com.itirafapp.android.data.remote.notification.dto.NotificationSeenRequest
import com.itirafapp.android.data.remote.notification.dto.NotificationStatusResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Query

interface NotificationService {
    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("notifications/status")
    suspend fun fetchNotificationStatus(): NotificationStatusResponse

    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("notifications")
    suspend fun fetchNotifications(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): NotificationResponse

    @Headers("X-Auth-Restriction: NonAnonymous")
    @PUT("notifications/seen")
    suspend fun updateNotificationSeen(
        @Body request: NotificationSeenRequest
    ): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @PUT("notifications/seen/all")
    suspend fun updateNotificationSeenAll(): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @HTTP(method = "DELETE", path = "notifications", hasBody = true)
    suspend fun deleteNotification(
        @Body request: NotificationDeleteRequest
    ): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @DELETE("notifications/all")
    suspend fun deleteNotificationAll(): Unit

    @GET("users/me/notification-preferences")
    suspend fun fetchNotificationPreferences(): NotificationPreferencesResponse

    @PUT("users/me/notification-preferences")
    suspend fun updateNotificationPreferences(
        @Body request: NotificationPreferencesRequest
    ): Unit
}