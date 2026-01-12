package com.itirafapp.android.data.remote

import com.itirafapp.android.data.remote.dto.UserResponse
import retrofit2.http.GET

interface UserService {
    @GET("user/me")
    suspend fun getMe(): UserResponse
}