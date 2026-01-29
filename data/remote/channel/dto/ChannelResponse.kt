package com.itirafapp.android.data.remote.channel.dto

import com.google.gson.annotations.SerializedName

data class ChannelResponse(
    val page: Int,
    val limit: Int,
    val totalRows: Int,
    val totalPages: Int,
    val data: List<ChannelDataResponse>
)

data class ChannelDataResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("imageURL") val imageURL: String?
)
