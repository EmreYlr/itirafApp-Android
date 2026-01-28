package com.itirafapp.android.presentation.mapper

import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.presentation.model.ChannelUiModel

fun ChannelData.toUiModel(isFollowing: Boolean): ChannelUiModel {
    return ChannelUiModel(
        id = this.id,
        title = this.title,
        description = this.description,
        imageURL = this.imageURL,
        isFollowing = isFollowing
    )
}

fun List<ChannelData>.toUiModels(followedIds: Set<Int>): List<ChannelUiModel> {
    return this.map { channel ->
        channel.toUiModel(isFollowing = followedIds.contains(channel.id))
    }
}