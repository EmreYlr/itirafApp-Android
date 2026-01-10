package com.itirafapp.android.data.remote.dto

data class AnonymousRegisterResponse(
    val email: String
)

data class AnonymousLoginRequest(
    val email: String
)