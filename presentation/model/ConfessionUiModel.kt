package com.itirafapp.android.presentation.model

import com.itirafapp.android.domain.model.ChannelData

data class ConfessionUiModel(
    val id: Int,
    val title: String,
    val message: String,
    val liked: Boolean,
    val likeCount: Int,
    val replyCount: Int,
    val createdAt: String,
    val owner: OwnerUiModel,
    val channel: ChannelData,
    val isNsfw: Boolean,
)

data class OwnerUiModel(
    val id: String,
    val username: String
)