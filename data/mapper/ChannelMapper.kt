package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.channel.dto.ChannelDataResponse
import com.itirafapp.android.data.remote.channel.dto.ChannelResponse
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.model.PaginatedResult


fun ChannelResponse.toDomain(): PaginatedResult<ChannelData> {
    return PaginatedResult(
        items = this.data.map { it.toDomain() },

        page = this.page,
        totalPages = this.totalPages,

        hasNextPage = this.page < this.totalPages
    )
}
fun ChannelDataResponse.toDomain(): ChannelData {
    return ChannelData(
        id = this.id,
        title = this.title,
        description = this.description,
        imageURL = this.imageURL ?: ""
    )
}