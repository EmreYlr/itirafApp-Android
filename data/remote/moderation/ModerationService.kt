package com.itirafapp.android.data.remote.moderation

import com.itirafapp.android.data.remote.moderation.dto.ModerationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ModerationService {
    @GET("moderation/pending")
    suspend fun fetchPendingModerationRequests(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ModerationResponse
}