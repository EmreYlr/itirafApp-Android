package com.itirafapp.android.domain.model

data class BlockedUsers(
    val userId: String,
    val username: String,
    val blockedAt: String
)