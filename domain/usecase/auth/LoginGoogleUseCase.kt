package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.data.remote.auth.dto.GoogleLoginRequest
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSessionManager: UserSessionManager
) {
    operator fun invoke(request: GoogleLoginRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)

        val loginResult = authRepository.googleLogin(request)

        if (loginResult is Resource.Error) {
            emit(Resource.Error(loginResult.error))
            return@flow
        }

        emit(userSessionManager.setupUserSession())
    }
}