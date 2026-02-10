package com.itirafapp.android.presentation.screens.profile.settings.notification

import com.itirafapp.android.domain.model.NotificationEventType
import com.itirafapp.android.presentation.model.NotificationPreferencesUiModel
import com.itirafapp.android.util.state.UiText

data class NotificationPreferencesState(
    val isLoading: Boolean = false,
    val isMasterEnabled: Boolean = true,
    val notificationItems: List<NotificationPreferencesUiModel> = emptyList()
)

sealed class NotificationPreferencesEvent {
    object OnBackClicked : NotificationPreferencesEvent()
    data class OnMasterSwitchChanged(val isEnabled: Boolean) : NotificationPreferencesEvent()
    data class OnItemSwitchChanged(val type: NotificationEventType, val isEnabled: Boolean) :
        NotificationPreferencesEvent()
}

sealed class NotificationPreferencesUiEvent {
    object NavigateToBack : NotificationPreferencesUiEvent()
    data class RequestSystemPermission(val message: String) : NotificationPreferencesUiEvent()
    data class ShowMessage(val message: UiText) : NotificationPreferencesUiEvent()
}