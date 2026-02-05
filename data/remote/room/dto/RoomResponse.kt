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
    val confessionTitle: String,
    val confessionMessage: String,
    val channelMessageId: Long,
    val createdAt: String,
    val status: String
)
