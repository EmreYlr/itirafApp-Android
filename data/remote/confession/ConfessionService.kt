package com.itirafapp.android.data.remote.confession

import com.itirafapp.android.data.remote.confession.dto.ConfessionResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ConfessionService {
    @GET("messages")
    suspend fun fetchFlow(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ConfessionResponse

    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("users/me/channels/messages")
    suspend fun fetchFollowingFlow(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ConfessionResponse

    @POST("messages/{id}/likes")
    suspend fun likeConfession(
        @Path("id") id: Int
    ): Unit

    @DELETE("messages/{id}/likes")
    suspend fun unlikeConfession(
        @Path("id") id: Int
    ): Unit
}