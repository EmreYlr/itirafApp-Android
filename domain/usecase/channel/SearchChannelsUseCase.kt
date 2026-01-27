package com.itirafapp.android.domain.usecase.channel

import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.repository.ChannelRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository
) {
    operator fun invoke(query: String): Flow<Resource<List<ChannelData>>> = flow {
        emit(Resource.Loading())
        try {
            val result = repository.searchChannels(query)
            emit(result)
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Beklenmedik hata"))
        }
    }
}