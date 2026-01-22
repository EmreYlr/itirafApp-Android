package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.manager.TokenManager
import com.itirafapp.android.util.state.AuthState
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) {
    operator fun invoke(): AuthState {
        val accessToken = tokenManager.getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            return AuthState.UNAUTHENTICATED
        }

        val user = userRepository.getLocalUser()

        return when {
            user == null -> AuthState.UNAUTHENTICATED
            user.anonymous -> AuthState.ANONYMOUS
            else -> AuthState.AUTHENTICATED
        }
    }
}