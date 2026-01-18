package com.itirafapp.android.data.remote.auth

import com.itirafapp.android.data.remote.auth.dto.AnonymousLoginRequest
import com.itirafapp.android.data.remote.auth.dto.AnonymousRegisterResponse
import com.itirafapp.android.data.remote.auth.dto.AuthResponse
import com.itirafapp.android.data.remote.auth.dto.LoginRequest
import com.itirafapp.android.data.remote.auth.dto.RefreshTokenRequest
import com.itirafapp.android.data.remote.auth.dto.RegisterRequest
import com.itirafapp.android.data.remote.auth.dto.ResetPasswordRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthService {
    @POST("auth/tokens/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): AuthResponse

    @POST("auth/register/anonymous")
    suspend fun registerAnonymous(): AnonymousRegisterResponse

    @POST("auth/login/anonymous")
    suspend fun loginAnonymous(
        @Body request: AnonymousLoginRequest
    ): AuthResponse

    @POST("auth/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    )

    @POST("auth/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): AuthResponse

    @POST("auth/forgot-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    )
    @DELETE("auth/logout")
    suspend fun logoutUser()
}