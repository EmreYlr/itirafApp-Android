package com.itirafapp.android.data.remote.auth.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
)

data class AnonymousRegisterResponse(
    val email: String
)