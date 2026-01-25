package com.itirafapp.android.data.remote.confession.dto

import com.itirafapp.android.data.remote.channel.dto.ChannelDataResponse

data class ConfessionDetailResponse(
    val id: Int,
    val title: String? = null,
    val message: String,
    val liked: Boolean,
    val likeCount: Int,
    val replyCount: Int,
    val shareCount: Int,
    val createdAt: String,
    val owner: OwnerResponse,
    val channel: ChannelDataResponse,
    val shortlink: String?,
    val replies: List<ReplyResponse>,
    val isNsfw: Boolean,
)

data class ReplyResponse(
    val id: Int,
    val message: String,
    val owner: OwnerResponse,
    val createdAt: String,
)

data class ShortlinkResponse(
    val url: String
)