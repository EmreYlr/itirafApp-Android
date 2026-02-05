package com.itirafapp.android.domain.model

data class SentMessage(
    val requestId: String,
    val confessionAuthorUsername: String,
    val initialMessage: String,
    val confessionTitle: String,
    val confessionMessage: String,
    val channelMessageId: Long,
    val createdAt: String,
    val status: SentMessageStatus
)

enum class SentMessageStatus {
    PENDING,
    REJECTED
}