package com.itirafapp.android.domain.usecase.follow

import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFollowedChannelsUseCase @Inject constructor(
    private val followRepository: FollowRepository
) {
    operator fun invoke(): Flow<Resource<List<ChannelData>>> = flow {
        emit(Resource.Loading())

        val result = followRepository.getRemoteFollowedChannels()

        emit(result)
    }
}