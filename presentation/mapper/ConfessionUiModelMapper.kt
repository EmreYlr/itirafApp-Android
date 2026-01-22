package com.itirafapp.android.presentation.mapper

import com.itirafapp.android.R
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.model.ConfessionData
import com.itirafapp.android.domain.model.Owner
import com.itirafapp.android.presentation.model.ConfessionUiModel
import com.itirafapp.android.presentation.model.OwnerUiModel
import com.itirafapp.android.util.extension.formatToRelativeTime
import com.itirafapp.android.util.state.UiText

fun ConfessionData.toUiModel(currentUserId: String?): ConfessionUiModel {
    val isOwner = this.owner.id == currentUserId

    return ConfessionUiModel(
        id = this.id,
        title = this.title ?: "",
        message = this.message,
        liked = this.liked,
        likeCount = this.likeCount,
        replyCount = this.replyCount,
        createdAt = formatToRelativeTime(this.createdAt),
        owner = this.owner.toUiModel(isOwner),
        channel = this.channel ?: ChannelData(0, title = "Unowned", "", imageURL = ""),
        isNsfw = this.isNsfw,
        isMine = isOwner
    )
}

fun Owner.toUiModel(isMine: Boolean): OwnerUiModel {
    return OwnerUiModel(
        id = this.id,
        username = if (isMine) {
            UiText.StringResource(R.string.confession_owner_you)
        } else {
            UiText.DynamicString(this.username ?: "Anonymous")
        }
    )
}