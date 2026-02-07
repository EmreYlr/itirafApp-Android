package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.room.dto.MessageDataResponse
import com.itirafapp.android.data.remote.room.dto.RoomMessagesResponse
import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.domain.model.PaginatedResult

fun RoomMessagesResponse.toDomain(): PaginatedResult<MessageData> {
    return PaginatedResult(
        items = this.data.map { it.toDomain() },

        page = this.page,
        totalPages = this.totalPages,

        hasNextPage = this.page < this.totalPages
    )
}

fun MessageDataResponse.toDomain(): MessageData {
    return MessageData(
        id = this.id,
        content = this.content,
        createdAt = this.createdAt,
        isMyMessage = this.isMyMessage,
        isSeen = this.seenAt != null,
        seenAt = this.seenAt
    )
}


