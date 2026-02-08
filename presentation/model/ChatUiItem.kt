package com.itirafapp.android.presentation.model

import com.itirafapp.android.domain.model.MessageData

sealed class ChatUiItem {
    data class MessageItem(
        val message: MessageData,
        val showTime: Boolean,
        val showProfileImage: Boolean = false
    ) : ChatUiItem()

    data class DateSeparator(
        val dateText: String
    ) : ChatUiItem()
}