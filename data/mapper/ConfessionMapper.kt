package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.confession.dto.ConfessionDataResponse
import com.itirafapp.android.data.remote.confession.dto.ConfessionResponse
import com.itirafapp.android.data.remote.confession.dto.OwnerResponse
import com.itirafapp.android.domain.model.ConfessionData
import com.itirafapp.android.domain.model.Owner
import com.itirafapp.android.domain.model.PaginatedResult

fun ConfessionResponse.toDomain(): PaginatedResult<ConfessionData> {
    return PaginatedResult(
        items = this.data.map { it.toDomain() },

        page = this.page,
        totalPages = this.totalPages,

        hasNextPage = this.page < this.totalPages
    )
}

fun ConfessionDataResponse.toDomain(): ConfessionData {
    return ConfessionData(
        id = this.id,
        title = this.title,
        message = this.message,
        liked = this.liked,
        likeCount = this.likeCount,
        replyCount = this.replyCount,
        shareCount = this.shareCount,
        createdAt = this.createdAt,
        owner = this.owner.toDomain(),
        channel = this.channel?.toDomain(),
        isNsfw = this.isNsfw
    )
}

fun OwnerResponse.toDomain(): Owner {
    return Owner(
        id = this.id,
        username = this.username
    )
}

