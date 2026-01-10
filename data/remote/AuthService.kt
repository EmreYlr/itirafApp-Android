package com.itirafapp.android.data.remote

import com.itirafapp.android.data.remote.dto.AnonymousLoginRequest
import com.itirafapp.android.data.remote.dto.AnonymousRegisterResponse
import com.itirafapp.android.data.remote.dto.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/register/anonymous")
    suspend fun registerAnonymous(): AnonymousRegisterResponse

    @POST("auth/login/anonymous")
    suspend fun loginAnonymous(
        @Body request: AnonymousLoginRequest
    ): AuthResponse
}