package com.itirafapp.android.data.remote

import com.itirafapp.android.data.remote.dto.AnonymousLoginRequest
import com.itirafapp.android.data.remote.dto.AnonymousRegisterResponse
import com.itirafapp.android.data.remote.dto.AuthResponse
import com.itirafapp.android.data.remote.dto.LoginRequest
import com.itirafapp.android.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
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
}