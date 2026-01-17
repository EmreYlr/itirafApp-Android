package com.itirafapp.android.presentation.screens.profile.settings

data class SettingsState(
    val isLoading: Boolean = false,
)

sealed class SettingsEvent {
    object LogoutClicked : SettingsEvent()
}

sealed class SettingsUiEvent {
    object NavigateToLogin : SettingsUiEvent()
}