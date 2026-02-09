package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.domain.repository.CrashReporter
import com.itirafapp.android.domain.repository.DeviceRepository
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.domain.repository.SessionTracker
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LogoutUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val deviceRepository: DeviceRepository,
    private val crashReporter: CrashReporter,
    private val sessionTracker: SessionTracker
) {
    operator fun invoke(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val logoutResult = authRepository.logoutUser()

        deviceRepository.clearLocalDeviceData()

        userRepository.clearUserData()

        followRepository.clearCache()

        crashReporter.setUserId("")

        sessionTracker.clearUser()

        if (logoutResult is Resource.Error) {
            emit(Resource.Error(logoutResult.message ?: "Çıkış yapılamadı"))
            return@flow
        }

        emit(Resource.Success(Unit))
    }
}