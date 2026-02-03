package com.itirafapp.android.presentation.model

import com.itirafapp.android.domain.model.NotificationEventType
import com.itirafapp.android.util.state.UiText

data class NotificationPreferencesUiModel(
    val type: NotificationEventType,
    val title: UiText,
    val description: UiText,
    val isEnabled: Boolean
)