package com.itirafapp.android.presentation.mapper

import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.presentation.model.ChatUiItem
import com.itirafapp.android.util.extension.formatToRelativeTime

fun MessageData.toUiModel(showTime: Boolean, showProfileImage: Boolean): ChatUiItem {
    val formattedTime = formatToRelativeTime(this.createdAt)
    return ChatUiItem(
        message = this.copy(createdAt = formattedTime),
        showTime = showTime,
        showProfileImage = showProfileImage
    )
}