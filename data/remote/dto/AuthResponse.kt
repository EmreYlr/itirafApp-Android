package com.itirafapp.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
)