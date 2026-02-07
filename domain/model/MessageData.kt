package com.itirafapp.android.domain.model

data class MessageData(
    val id: Int,
    val content: String,
    val createdAt: String,
    val isMyMessage: Boolean,
    val isSeen: Boolean,
    val seenAt: String?
)