package com.itirafapp.android.data.repository

import com.itirafapp.android.data.remote.AuthService
import com.itirafapp.android.data.remote.dto.AnonymousLoginRequest
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.Resource
import com.itirafapp.android.util.TokenManager
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService,
    private val tokenManager: TokenManager
) : BaseRepository(), AuthRepository {

    override suspend fun loginAnonymous(): Resource<Unit> {
        return safeApiCall {
            val email = tokenManager.getAnonEmail() ?: run {
                val register = api.registerAnonymous()
                tokenManager.saveAnonEmail(register.email)
                register.email
            }

            val response = api.loginAnonymous(AnonymousLoginRequest(email))
            tokenManager.saveTokens(response.accessToken, response.refreshToken)

            Unit
        }
    }
}