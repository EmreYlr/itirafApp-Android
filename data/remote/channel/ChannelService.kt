package com.itirafapp.android.data.remote.channel

import com.itirafapp.android.data.remote.channel.dto.ChannelDataResponse
import com.itirafapp.android.data.remote.channel.dto.ChannelResponse
import retrofit2.http.GET
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
}


