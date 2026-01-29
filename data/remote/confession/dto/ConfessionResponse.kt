package com.itirafapp.android.data.remote.confession.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.itirafapp.android.data.remote.channel.dto.ChannelDataResponse
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class OwnerResponse(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String?
) : Parcelable