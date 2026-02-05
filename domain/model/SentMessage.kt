package com.itirafapp.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SentMessage(
    val requestId: String,
    val confessionAuthorUsername: String,
    val initialMessage: String,
    val confessionTitle: String,
    val confessionMessage: String,
    val channelMessageId: Int,
    val createdAt: String,
    val status: SentMessageStatus
) : Parcelable

enum class SentMessageStatus {
    PENDING,
    REJECTED
}