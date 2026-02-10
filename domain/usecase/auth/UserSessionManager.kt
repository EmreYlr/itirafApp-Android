package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.domain.repository.CrashReporter
import com.itirafapp.android.domain.repository.DeviceRepository
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.domain.repository.SessionTracker
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionManager @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val deviceRepository: DeviceRepository,
    private val crashReporter: CrashReporter,
    private val sessionTracker: SessionTracker
) {

    suspend fun setupUserSession(): Resource<Unit> {
        cleanUserData()

        val profileResult = userRepository.getUser()

        return if (profileResult is Resource.Success) {
            val user = profileResult.data

            user?.let {
                val userId = it.id.toString()
                crashReporter.setUserId(userId)
                crashReporter.setUserAnonymous(false)
                sessionTracker.setUserId(userId)
            }

            followRepository.syncFollowedChannels()

            Resource.Success(Unit)
        } else {
            Resource.Error(profileResult.message ?: "Profil bilgileri alınamadı")
        }
    }

    private suspend fun cleanUserData() {
        deviceRepository.clearLocalDeviceData()
        userRepository.clearUserData()
        followRepository.clearCache()

        crashReporter.setUserId("")
        sessionTracker.clearUser()
    }
}