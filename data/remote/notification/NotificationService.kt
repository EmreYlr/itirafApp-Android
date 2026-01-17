package com.itirafapp.android.data.remote.notification

import com.itirafapp.android.data.remote.notification.dto.NotificationStatusResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface NotificationService {
    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("notifications/status")
    suspend fun fetchNotificationStatus(): NotificationStatusResponse

}