package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.domain.repository.CrashReporter
import com.itirafapp.android.domain.repository.DeviceRepository
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.domain.repository.UserRepository
import javax.inject.Inject

class LogoutAnonymousUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val deviceRepository: DeviceRepository,
    private val crashReporter: CrashReporter
) {
    suspend operator fun invoke() {
        deviceRepository.clearLocalDeviceData()
        userRepository.clearUserData()
        followRepository.clearCache()
        crashReporter.setUserId("")
    }
}