package com.itirafapp.android.data.remote.user.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.itirafapp.android.data.remote.channel.dto.ChannelDataResponse
import com.itirafapp.android.data.remote.confession.dto.ReplyResponse
import com.itirafapp.android.domain.model.enums.ModerationStatus
import com.itirafapp.android.domain.model.enums.Violation
import kotlinx.parcelize.Parcelize

data class MyConfessionResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("totalRows") val totalRows: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("data") val data: List<MyConfessionDataResponse>
)

@Parcelize
data class MyConfessionDataResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("message") val message: String,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("liked") val isLiked: Boolean,
    @SerializedName("replyCount") val replyCount: Int,
    @SerializedName("shareCount") val shareCount: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("channel") val channel: ChannelDataResponse,
    @SerializedName("replies") val replies: List<ReplyResponse>?,
    @SerializedName("rejectionReason") val rejectionReason: String?,
    @SerializedName("violations") val violations: List<Violation>?,
    @SerializedName("moderationStatus") val moderationStatus: ModerationStatus,
    @SerializedName("isNsfw") val isNsfw: Boolean
) : Parcelable
