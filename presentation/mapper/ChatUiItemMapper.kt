package com.itirafapp.android.presentation.mapper

import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.presentation.model.ChatUiItem

fun MessageData.toUiModel(showTime: Boolean): ChatUiItem {
    return ChatUiItem(
        message = this,
        showTime = showTime
    )
}