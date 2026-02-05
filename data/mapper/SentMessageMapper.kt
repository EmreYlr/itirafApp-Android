package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.room.dto.SentMessageResponse
import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.domain.model.SentMessageStatus
import com.itirafapp.android.util.extension.formatToRelativeTime

fun SentMessageResponse.toDomain(): SentMessage {
    return SentMessage(
        requestId = this.requestId,
        confessionAuthorUsername = this.confessionAuthorUsername,
        initialMessage = this.initialMessage,
        confessionTitle = this.confessionTitle,
        confessionMessage = this.confessionMessage,
        channelMessageId = this.channelMessageId,
        createdAt = formatToRelativeTime(this.createdAt),
        status = this.status.toSentMessageStatus()
    )
}

fun String.toSentMessageStatus(): SentMessageStatus {
    return try {
        SentMessageStatus.valueOf(this.uppercase())
    } catch (e: IllegalArgumentException) {
        SentMessageStatus.PENDING
    }
}
