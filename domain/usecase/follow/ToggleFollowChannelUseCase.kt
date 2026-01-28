package com.itirafapp.android.domain.usecase.follow

import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class ToggleFollowChannelUseCase @Inject constructor(
    private val followRepository: FollowRepository
) {
    suspend operator fun invoke(channel: ChannelData): Resource<Unit> {
        val isFollowed = followRepository.isChannelFollowed(channel.id)

        return if (isFollowed) {
            followRepository.unfollowChannel(channel)
        } else {
            followRepository.followChannel(channel)
        }
    }
}