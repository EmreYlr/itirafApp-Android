package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.util.state.Resource

interface ChannelRepository {
    suspend fun getChannels(page: Int, limit: Int): Resource<PaginatedResult<ChannelData>>
    suspend fun searchChannels(query: String): Resource<List<ChannelData>>
}