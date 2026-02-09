package com.itirafapp.android.data.repository

import com.itirafapp.android.data.remote.auth.AuthService
import com.itirafapp.android.data.remote.auth.dto.AnonymousLoginRequest
import com.itirafapp.android.data.remote.auth.dto.AnonymousRegisterResponse
import com.itirafapp.android.data.remote.auth.dto.GoogleLoginRequest
import com.itirafapp.android.data.remote.auth.dto.LoginRequest
import com.itirafapp.android.data.remote.auth.dto.RegisterRequest
import com.itirafapp.android.data.remote.auth.dto.ResetPasswordRequest
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.manager.TokenManager
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun loginUser(request: LoginRequest): Resource<Unit> {
        return safeApiCall {
            val response = api.loginUser(request)
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
        }
    }

    override suspend fun googleLogin(request: GoogleLoginRequest): Resource<Unit> {
        return safeApiCall {
            val response = api.googleLogin(request)
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
        }
    }

    override suspend fun registerUser(request: RegisterRequest): Resource<Unit> {
        return safeApiCall {
            api.registerUser(request)
        }
    }

    override suspend fun registerAnonymous(): Resource<AnonymousRegisterResponse> {
        return safeApiCall {
            api.registerAnonymous()
        }
    }

    override suspend fun loginAnonymous(request: AnonymousLoginRequest): Resource<Unit> {
        return safeApiCall {
            val request = AnonymousLoginRequest(request.email)
            val response = api.loginAnonymous(request)
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
        }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): Resource<Unit> {
        return safeApiCall {
            api.resetPassword(request)
        }
    }

    override suspend fun logoutUser(): Resource<Unit> {
        return safeApiCall {
            api.logoutUser()
            tokenManager.deleteTokens()
        }
    }
}