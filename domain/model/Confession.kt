package com.itirafapp.android.domain.model

data class ConfessionData(
    val id: Int,
    val title: String? = null,
    val message: String,
    val liked: Boolean,
    val likeCount: Int,
    val replyCount: Int,
    val shareCount: Int,
    val createdAt: String,
    val owner: Owner,
    val channel: ChannelData?,
    val isNsfw: Boolean,
)

data class Owner(
    val id: String,
    val username: String?
)