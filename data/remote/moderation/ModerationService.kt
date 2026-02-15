package com.itirafapp.android.data.remote.moderation

import com.itirafapp.android.data.remote.moderation.dto.ModerationRequest
import com.itirafapp.android.data.remote.moderation.dto.ModerationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ModerationService {
    @GET("moderation/pending")
    suspend fun fetchPendingModerationRequests(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ModerationResponse

    @POST("moderation/messages/{id}")
    suspend fun postModerationMessage(
        @Path("id") id: Int,
        @Body moderationRequest: ModerationRequest
    ): Unit
}