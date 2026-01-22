package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.confession.dto.ConfessionDetailResponse
import com.itirafapp.android.data.remote.confession.dto.ReplyResponse
import com.itirafapp.android.domain.model.ConfessionDetail
import com.itirafapp.android.domain.model.Reply

fun ConfessionDetailResponse.toDomain(): ConfessionDetail {
    return ConfessionDetail(
        id = this.id,
        title = this.title,
        message = this.message,
        liked = this.liked,
        likeCount = this.likeCount,
        replyCount = this.replyCount,
        shareCount = this.shareCount,
        createdAt = this.createdAt,
        owner = this.owner.toDomain(),
        channel = this.channel.toDomain(),
        shortLink = this.shortLink,
        replies = this.replies.map { it.toDomain() },
        isNsfw = this.isNsfw
    )
}

fun ReplyResponse.toDomain(): Reply {
    return Reply(
        id = this.id,
        message = this.message,
        owner = this.owner.toDomain(),
        createdAt = this.createdAt,
    )
}