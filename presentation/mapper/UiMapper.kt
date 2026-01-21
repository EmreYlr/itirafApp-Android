package com.itirafapp.android.presentation.mapper

import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.model.ConfessionData
import com.itirafapp.android.domain.model.Owner
import com.itirafapp.android.presentation.model.ConfessionUiModel
import com.itirafapp.android.presentation.model.OwnerUiModel
import com.itirafapp.android.util.formatToRelativeTime

fun ConfessionData.toUiModel(): ConfessionUiModel {
    return ConfessionUiModel(
        id = this.id,
        title = this.title ?: "",
        message = this.message,
        liked = this.liked,
        likeCount = this.likeCount,
        replyCount = this.replyCount,
        createdAt = formatToRelativeTime(this.createdAt),
        owner = this.owner.toUiModel(),
        channel = this.channel ?: ChannelData(0, title = "Unowned", "", imageURL = ""),
        isNsfw = this.isNsfw
    )
}

fun Owner.toUiModel(): OwnerUiModel {
    return OwnerUiModel(
        id = this.id,
        username = this.username ?: "Anonim Kullanıcı"
    )
}
