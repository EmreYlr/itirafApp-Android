package com.itirafapp.android.data.repository

import com.itirafapp.android.data.remote.channel.ChannelService
import com.itirafapp.android.data.remote.channel.dto.FollowRequest
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.util.manager.FollowPreferencesManager
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val channelService: ChannelService,
    private val preferencesManager: FollowPreferencesManager
) : FollowRepository {

    override val followedChannels: Flow<List<ChannelData>> = preferencesManager.followedChannels

    override suspend fun syncFollowedChannels(): Resource<Unit> {
        return safeApiCall {
            val remoteChannels = channelService.getFollowedChannels()
            preferencesManager.updateFollowedChannels(remoteChannels)
            Unit
        }
    }

    override suspend fun getRemoteFollowedChannels(): Resource<List<ChannelData>> {
        return safeApiCall {
            val remoteChannels = channelService.getFollowedChannels()
            preferencesManager.updateFollowedChannels(remoteChannels)
            remoteChannels
        }
    }

    override suspend fun followChannel(channel: ChannelData): Resource<Unit> {
        preferencesManager.addChannel(channel)

        val result = safeApiCall {
            val request = FollowRequest(channelIds = listOf(channel.id))
            channelService.followChannel(request)
        }

        if (result is Resource.Error) {
            preferencesManager.removeChannel(channel.id)
        }

        return result
    }

    override suspend fun unfollowChannel(channel: ChannelData): Resource<Unit> {
        preferencesManager.removeChannel(channel.id)

        val result = safeApiCall {
            channelService.unfollowChannel(channel.id)
        }

        if (result is Resource.Error) {
            preferencesManager.addChannel(channel)
        }

        return result
    }

    override fun isChannelFollowed(channelId: Int): Boolean {
        return preferencesManager.followedChannels.value.any { it.id == channelId }
    }

    override fun clearCache() {
        preferencesManager.clear()
    }
}