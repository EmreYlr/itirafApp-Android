package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.user.dto.MyConfessionDataResponse
import com.itirafapp.android.data.remote.user.dto.MyConfessionResponse
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.util.extension.formatToRelativeTime

fun MyConfessionResponse.toDomain(): PaginatedResult<MyConfessionData> {
    return PaginatedResult(
        items = this.data.map { it.toDomain() },

        page = this.page,
        totalPages = this.totalPages,

        hasNextPage = this.page < this.totalPages
    )
}

fun MyConfessionDataResponse.toDomain(): MyConfessionData {
    return MyConfessionData(
        id = this.id,
        title = this.title,
        message = this.message,
        isLiked = this.isLiked,
        likeCount = this.likeCount,
        replyCount = this.replyCount,
        shareCount = this.shareCount,
        createdAt = formatToRelativeTime(this.createdAt),
        channel = this.channel.toDomain(),
        reply = this.replies?.map { it.toDomain() } ?: emptyList(),
        rejectionReason = this.rejectionReason,
        violations = this.violations,
        moderationStatus = this.moderationStatus,
        isNsfw = this.isNsfw
    )
}

