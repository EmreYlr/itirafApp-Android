package com.itirafapp.android.domain.usecase.follow

import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class SyncFollowedChannelsUseCase @Inject constructor(
    private val followRepository: FollowRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return followRepository.syncFollowedChannels()
    }
}