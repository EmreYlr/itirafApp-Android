package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface FollowRepository {
    val followedChannels: Flow<List<ChannelData>>

    suspend fun syncFollowedChannels(): Resource<Unit>

    suspend fun followChannel(channel: ChannelData): Resource<Unit>
    suspend fun unfollowChannel(channel: ChannelData): Resource<Unit>

    fun isChannelFollowed(channelId: Int): Boolean
    fun clearCache()
}