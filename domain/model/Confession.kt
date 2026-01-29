package com.itirafapp.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class Owner(
    val id: String,
    val username: String?
) : Parcelable