package com.itirafapp.android.data.remote.dto

data class AnonymousRegisterResponse(
    val email: String
)

data class AnonymousLoginRequest(
    val email: String
)

data class RegisterRequest(
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)