package com.itirafapp.android.presentation.mapper

import com.itirafapp.android.domain.model.ConfessionDetail
import com.itirafapp.android.domain.model.Reply
import com.itirafapp.android.presentation.model.ConfessionDetailUiModel
import com.itirafapp.android.presentation.model.ReplyUiModel
import com.itirafapp.android.util.extension.formatToRelativeTime

fun ConfessionDetail.toUiModel(currentUserId: String?): ConfessionDetailUiModel {
    val isOwner = this.owner.id == currentUserId

    return ConfessionDetailUiModel(
        id = this.id,
        title = this.title ?: "",
        message = this.message,
        liked = this.liked,
        likeCount = this.likeCount,
        replyCount = this.replyCount,
        shareCount = this.shareCount,
        createdAt = formatToRelativeTime(this.createdAt),
        owner = this.owner.toUiModel(isOwner),
        channel = this.channel,
        replies = this.replies.map { it.toUiModel(currentUserId) },
        isNsfw = this.isNsfw,
        isMine = isOwner,
        shortlink = this.shortlink
    )
}

fun Reply.toUiModel(currentUserId: String?): ReplyUiModel {
    val isOwner = this.owner.id == currentUserId
    return ReplyUiModel(
        id = this.id,
        message = this.message,
        owner = this.owner.toUiModel(isOwner),
        createdAt = formatToRelativeTime(this.createdAt),
        isMine = isOwner
    )
}
