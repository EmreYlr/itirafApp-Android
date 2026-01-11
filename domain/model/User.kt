package com.itirafapp.android.domain.model

data class User(
    val id: String? = null,
    val username: String? = null,
    val email: String,
    val anonymous: Boolean,
)