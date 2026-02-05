package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.room.dto.DirectMessageResponse
import com.itirafapp.android.domain.model.DirectMessage
import com.itirafapp.android.util.extension.formatToRelativeTime

fun DirectMessageResponse.toDomain(): DirectMessage {
    return DirectMessage(
        roomId = this.roomId,
        username = this.username,
        lastMessage = this.lastMessage,
        lastMessageDate = formatToRelativeTime(this.lastMessageDate),
        isLastMessageMine = this.isLastMessageMine,
        unreadMessageCount = this.unreadMessageCount
    )
}