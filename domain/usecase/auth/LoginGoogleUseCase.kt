package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.data.remote.auth.dto.GoogleLoginRequest
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.domain.repository.CrashReporter
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val crashReporter: CrashReporter
) {
    operator fun invoke(request: GoogleLoginRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val loginResult = authRepository.googleLogin(request)

        if (loginResult is Resource.Error) {
            emit(Resource.Error(loginResult.message ?: "Giriş yapılamadı"))
            return@flow
        }

        val profileResult = userRepository.getUser()

        if (profileResult is Resource.Success) {
            val user = profileResult.data

            user?.let {
                crashReporter.setUserId(it.id.toString())
                crashReporter.setUserAnonymous(false)
            }
            followRepository.syncFollowedChannels()

            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Error(profileResult.message ?: "Profil bilgileri alınamadı"))
        }
    }
}