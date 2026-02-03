package com.itirafapp.android.presentation.model

import com.itirafapp.android.domain.model.NotificationEventType
import com.itirafapp.android.util.state.UiText

data class NotificationUiModel(
    val type: NotificationEventType,
    val title: UiText,
    val description: UiText,
    val isEnabled: Boolean
)