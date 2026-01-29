package com.itirafapp.android.domain.model

import android.os.Parcelable
import com.itirafapp.android.domain.model.enums.ModerationStatus
import com.itirafapp.android.domain.model.enums.Violation
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyConfessionData(
    val id: Int,
    val title: String? = null,
    val message: String,
    val isLiked: Boolean,
    val likeCount: Int,
    val replyCount: Int,
    val shareCount: Int,
    val createdAt: String,
    val channel: ChannelData,
    val reply: List<Reply>,
    val rejectionReason: String?,
    val violations: List<Violation>?,
    val moderationStatus: ModerationStatus,
    val isNsfw: Boolean,
) : Parcelable