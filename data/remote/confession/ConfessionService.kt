package com.itirafapp.android.data.remote.confession

import com.itirafapp.android.data.remote.confession.dto.ConfessionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ConfessionService {
    @GET("messages")
    suspend fun fetchFlow(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ConfessionResponse
}