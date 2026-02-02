package com.itirafapp.android.presentation.model

import com.itirafapp.android.domain.model.enums.NotificationEventType

data class NotificationUiModel(
    val type: NotificationEventType,
    val title: String,
    val description: String,
    val isEnabled: Boolean
)