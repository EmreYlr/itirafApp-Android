package com.itirafapp.android.data.remote.auth.dto

data class RefreshTokenRequest(
    val refreshToken: String
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

data class ResetPasswordRequest(
    val email: String
)