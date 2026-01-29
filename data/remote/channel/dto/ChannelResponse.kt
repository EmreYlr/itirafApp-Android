package com.itirafapp.android.data.remote.channel.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ChannelResponse(
    val page: Int,
    val limit: Int,
    val totalRows: Int,
    val totalPages: Int,
    val data: List<ChannelDataResponse>
)

@Parcelize
data class ChannelDataResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("imageURL") val imageURL: String?
) : Parcelable
