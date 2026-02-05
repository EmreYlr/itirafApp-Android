package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.room.dto.InboxMessageResponse
import com.itirafapp.android.domain.model.InboxMessage
import com.itirafapp.android.util.extension.formatToRelativeTime

fun InboxMessageResponse.toDomain(): InboxMessage {
    return InboxMessage(
        requestId = this.requestId,
        roomId = this.roomId,
        requesterUsername = this.requesterUsername,
        requesterUserId = this.requesterUserId,
        requesterSocialLinks = this.requesterSocialLinks?.map { it.toDomain() } ?: emptyList(),
        initialMessage = this.initialMessage,
        confessionTitle = this.confessionTitle ?: "",
        confessionMessage = this.confessionMessage,
        channelMessageId = this.channelMessageId,
        createdAt = formatToRelativeTime(this.createdAt)
    )
}