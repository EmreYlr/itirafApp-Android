package com.itirafapp.android.data.remote.channel.dto

data class ChannelResponse(
    val page: Int,
    val limit: Int,
    val totalRows: Int,
    val totalPages: Int,
    val data: List<ChannelDataResponse>
)
data class ChannelDataResponse(
    val id: Int,
    val title: String,
    val description: String,
    val imageURL: String?
)
