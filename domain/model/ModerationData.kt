package com.itirafapp.android.domain.model

import android.os.Parcelable
import com.itirafapp.android.domain.model.enums.ModerationStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModerationData(
    val id: Int,
    val title: String,
    val message: String,
    val channelID: Int,
    val channelTitle: String,
    val ownerID: String,
    val ownerUsername: String,
    val moderationStatus: ModerationStatus,
    val rejectionReason: String,
    val createdAt: String,
    val isNsfw: Boolean
) : Parcelable
