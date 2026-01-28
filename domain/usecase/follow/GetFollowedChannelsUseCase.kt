package com.itirafapp.android.domain.usecase.follow

import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.repository.FollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFollowedChannelsUseCase @Inject constructor(
    private val followRepository: FollowRepository
) {
    operator fun invoke(): Flow<List<ChannelData>> {
        return followRepository.followedChannels
    }
}