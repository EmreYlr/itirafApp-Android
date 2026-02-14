package com.itirafapp.android.data.remote.room.dto

import com.itirafapp.android.data.remote.social_link.dto.LinkResponse

data class DirectMessageResponse(
    val roomId: String,
    val username: String,
    val lastMessage: String,
    val lastMessageDate: String,
    val isLastMessageMine: Boolean,
    val status: String,
    val unreadMessageCount: Int,
)

data class InboxMessageResponse(
    val requestId: String,
    val roomId: String,
    val requesterUsername: String,
    val requesterUserId: String,
    val requesterSocialLinks: List<LinkResponse>?,
    val initialMessage: String,
    val confessionTitle: String?,
    val confessionMessage: String,
    val channelMessageId: Int,
    val createdAt: String,
)

data class SentMessageResponse(
    val requestId: String,
    val confessionAuthorUsername: String,
    val initialMessage: String,
    val confessionTitle: String?,
    val confessionMessage: String,
    val channelMessageId: Int,
    val createdAt: String,
    val status: String
)

data class RoomMessagesResponse(
    val page: Int,
    val limit: Int,
    val totalRows: Int,
    val totalPages: Int,
    val data: List<MessageDataResponse>,
)

data class MessageDataResponse(
    val id: Int,
    val content: String,
    val createdAt: String,
    val isMyMessage: Boolean,
    val seenAt: String?
)