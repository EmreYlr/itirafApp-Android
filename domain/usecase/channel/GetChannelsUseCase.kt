package com.itirafapp.android.domain.usecase.channel

import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.ChannelRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository
) {
    operator fun invoke(
        page: Int,
        limit: Int = 15
    ): Flow<Resource<PaginatedResult<ChannelData>>> = flow {
        emit(Resource.Loading)

        try {
            val result = repository.getChannels(page = page, limit = limit)

            emit(result)

        } catch (e: Exception) {
            emit(Resource.Error(AppError.LocalError.Unknown))
        }
    }
}