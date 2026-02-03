package com.itirafapp.android.presentation.screens.profile.settings.notification

import com.itirafapp.android.domain.model.NotificationEventType
import com.itirafapp.android.presentation.model.NotificationUiModel

data class NotificationState(
    val isLoading: Boolean = false,
    val isMasterEnabled: Boolean = true,
    val notificationItems: List<NotificationUiModel> = emptyList()
)

sealed class NotificationEvent {
    object OnBackClicked : NotificationEvent()
    data class OnMasterSwitchChanged(val isEnabled: Boolean) : NotificationEvent()
    data class OnItemSwitchChanged(val type: NotificationEventType, val isEnabled: Boolean) :
        NotificationEvent()
}

sealed class NotificationUiEvent {
    object NavigateToBack : NotificationUiEvent()
    data class ShowMessage(val message: String) : NotificationUiEvent()
}