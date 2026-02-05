package com.itirafapp.android.domain.model

data class InboxMessage(
    val requestId: String,
    val roomId: String,
    val requesterUsername: String,
    val requesterUserId: String,
    val requesterSocialLinks: List<Link>,
    val initialMessage: String,
    val confessionTitle: String,
    val confessionMessage: String,
    val channelMessageId: Int,
    val createdAt: String
)