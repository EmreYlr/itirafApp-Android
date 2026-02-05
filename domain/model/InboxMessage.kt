package com.itirafapp.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable