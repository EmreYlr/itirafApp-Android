package com.itirafapp.android.data.remote.confession.dto

import com.itirafapp.android.data.remote.channel.dto.ChannelDataResponse

data class ConfessionResponse(
    val page: Int,
    val limit: Int,
    val totalRows: Int,
    val totalPages: Int,
    val data: List<ConfessionDataResponse>,
)

data class ConfessionDataResponse(
    val id: Int,
    val title: String? = null,
    val message: String,
    val liked: Boolean,
    val likeCount: Int,
    val replyCount: Int,
    val shareCount: Int,
    val createdAt: String,
    val owner: OwnerResponse,
    val channel: ChannelDataResponse?,
    val isNsfw: Boolean,
)

data class OwnerResponse(
    val id: String,
    val username: String?
)