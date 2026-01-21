package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.channel.dto.ChannelDataResponse
import com.itirafapp.android.domain.model.ChannelData

fun ChannelDataResponse.toDomain(): ChannelData {
    return ChannelData(
        id = this.id,
        title = this.title,
        description = this.description,
        imageURL = this.imageURL ?: ""
    )
}