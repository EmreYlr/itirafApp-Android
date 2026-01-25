package com.itirafapp.android.presentation.model

import com.itirafapp.android.domain.model.ChannelData

data class ConfessionDetailUiModel(
    val id: Int,
    val title: String,
    val message: String,
    val liked: Boolean,
    val likeCount: Int,
    val replyCount: Int,
    val shareCount: Int,
    val createdAt: String,
    val owner: OwnerUiModel,
    val channel: ChannelData,
    val shortlink: String?,
    val replies: List<ReplyUiModel>,
    val isNsfw: Boolean,
    val isMine: Boolean = false
)

data class ReplyUiModel(
    val id: Int,
    val message: String,
    val owner: OwnerUiModel,
    val createdAt: String,
)
