package com.itirafapp.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ConfessionDetail(
    val id: Int,
    val title: String? = null,
    val message: String,
    val liked: Boolean,
    val likeCount: Int,
    val replyCount: Int,
    val shareCount: Int,
    val createdAt: String,
    val owner: Owner,
    val channel: ChannelData,
    val shortlink: String?,
    val replies: List<Reply>,
    val isNsfw: Boolean,
)

@Parcelize
data class Reply(
    val id: Int,
    val message: String,
    val owner: Owner,
    val createdAt: String,
) : Parcelable
