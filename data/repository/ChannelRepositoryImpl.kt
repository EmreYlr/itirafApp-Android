package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.channel.ChannelService
import com.itirafapp.android.data.remote.channel.dto.ChannelResponse
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.ChannelRepository
import com.itirafapp.android.util.state.Resource
import com.itirafapp.android.util.state.map
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(
    private val api: ChannelService
) : ChannelRepository {
    override suspend fun getChannels(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ChannelData>> {
        val apiResult: Resource<ChannelResponse> = safeApiCall {
            api.fetchChannels(page = page, limit = limit)
        }

        return apiResult.map { response ->
            response.toDomain()
        }
    }

    override suspend fun searchChannels(query: String): Resource<List<ChannelData>> {
        return safeApiCall {
            val result = api.searchChannels(query)
            result.map { it.toDomain() }
        }
    }
}