package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.moderation.dto.ModerationDataResponse
import com.itirafapp.android.data.remote.moderation.dto.ModerationResponse
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.util.extension.formatToRelativeTime

fun ModerationResponse.toDomain(): PaginatedResult<ModerationData> {
    return PaginatedResult(
        items = this.data.map { it.toDomain() },

        page = this.page,
        totalPages = this.totalPages,

        hasNextPage = this.page < this.totalPages
    )
}

fun ModerationDataResponse.toDomain(): ModerationData {
    return ModerationData(
        id = this.id,
        title = this.title ?: "",
        message = this.message,
        channelID = this.channelID,
        channelTitle = this.channelTitle,
        ownerID = this.ownerID,
        ownerUsername = this.ownerUsername,
        moderationStatus = this.moderationStatus,
        rejectionReason = this.rejectionReason ?: "",
        createdAt = formatToRelativeTime(this.createdAt),
        isNsfw = this.isNsfw
    )
}