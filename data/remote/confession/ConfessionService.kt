package com.itirafapp.android.data.remote.confession

import com.itirafapp.android.data.remote.confession.dto.ConfessionDetailResponse
import com.itirafapp.android.data.remote.confession.dto.ConfessionResponse
import com.itirafapp.android.data.remote.confession.dto.ReplyRequest
import com.itirafapp.android.data.remote.confession.dto.ShortlinkRequest
import com.itirafapp.android.data.remote.confession.dto.ShortlinkResponse
import retrofit2.http.Body
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

    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("messages/{id}/replies")
    suspend fun repliesMessage(
        @Path("id") id: Int,
        @Body request: ReplyRequest
    ): Unit

    @POST("shortlinks")
    suspend fun repliesMessage(
        @Body request: ShortlinkRequest
    ): ShortlinkResponse

    @GET("messages/{id}")
    suspend fun fetchConfessionDetail(
        @Path("id") id: Int
    ): ConfessionDetailResponse

    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("messages/{id}/likes")
    suspend fun likeConfession(
        @Path("id") id: Int
    ): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @DELETE("messages/{id}/likes")
    suspend fun unlikeConfession(
        @Path("id") id: Int
    ): Unit
}