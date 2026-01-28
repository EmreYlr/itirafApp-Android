package com.itirafapp.android.data.remote.channel

import com.itirafapp.android.data.remote.channel.dto.ChannelDataResponse
import com.itirafapp.android.data.remote.channel.dto.ChannelResponse
import com.itirafapp.android.data.remote.channel.dto.FollowRequest
import com.itirafapp.android.domain.model.ChannelData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChannelService {
    @GET("channels")
    suspend fun fetchChannels(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ChannelResponse

    @GET("channels/search")
    suspend fun searchChannels(
        @Query("query") query: String
    ): List<ChannelDataResponse>

    @GET("users/me/channels")
    suspend fun getFollowedChannels(): List<ChannelData>

    @POST("users/me/channels/follow")
    suspend fun followChannel(
        @Body body: FollowRequest
    ): Unit

    @DELETE("users/me/channels/{id}")
    suspend fun unfollowChannel(
        @Path("id") id: Int
    ): Unit
}


