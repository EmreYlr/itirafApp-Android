package com.itirafapp.android.presentation.model

import com.itirafapp.android.domain.model.MessageData

data class ChatUiItem(
    val message: MessageData,
    val showTime: Boolean,
    val showProfileImage: Boolean = false
)