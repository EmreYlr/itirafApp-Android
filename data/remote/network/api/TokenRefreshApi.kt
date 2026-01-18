package com.itirafapp.android.data.remote.network.api

import com.itirafapp.android.data.remote.auth.dto.AuthResponse
import com.itirafapp.android.data.remote.auth.dto.RefreshTokenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenRefreshApi {
    @POST("auth/tokens/refresh")
    fun refreshToken(@Body request: RefreshTokenRequest): Call<AuthResponse>
}