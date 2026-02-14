package com.itirafapp.android.data.remote.moderation.dto

import com.google.gson.annotations.SerializedName
import com.itirafapp.android.domain.model.enums.ModerationStatus

data class ModerationResponse(
    val page: Int,
    val limit: Int,
    val totalRows: Int,
    val totalPages: Int,
    val data: List<ModerationDataResponse>
)

data class ModerationDataResponse(
    val id: Int,
    val title: String?,
    val message: String,
    @SerializedName("channelId")
    val channelID: Int,
    val channelTitle: String,
    @SerializedName("ownerId")
    val ownerID: String,
    val ownerUsername: String,
    val moderationStatus: ModerationStatus,
    val rejectionReason: String?,
    val createdAt: String,
    val isNsfw: Boolean
)
