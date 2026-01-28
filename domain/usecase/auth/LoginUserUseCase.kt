package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.data.remote.auth.dto.LoginRequest
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {
    operator fun invoke(request: LoginRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val loginResult = authRepository.loginUser(request)

        if (loginResult is Resource.Error) {
            emit(Resource.Error(loginResult.message ?: "Giriş yapılamadı"))
            return@flow
        }

        val profileResult = userRepository.getUser()

        if (profileResult is Resource.Success) {

            followRepository.syncFollowedChannels()

            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Error(profileResult.message ?: "Profil bilgileri alınamadı"))
        }
    }
}