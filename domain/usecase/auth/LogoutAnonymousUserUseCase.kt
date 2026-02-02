package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.domain.repository.UserRepository
import javax.inject.Inject

class LogoutAnonymousUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {
    suspend operator fun invoke() {
        userRepository.clearUserData()
        followRepository.clearCache()
    }
}