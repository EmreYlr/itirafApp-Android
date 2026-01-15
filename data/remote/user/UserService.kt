package com.itirafapp.android.data.remote.user

import com.itirafapp.android.data.remote.user.dto.UserResponse
import retrofit2.http.GET

interface UserService {
    @GET("users/me")
    suspend fun getMe(): UserResponse
}