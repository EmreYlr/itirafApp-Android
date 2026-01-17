package com.itirafapp.android.presentation.screens.profile

data class ProfileState(
    val isLoading: Boolean = false,
)

sealed class ProfileEvent {
    object SettingsClicked : ProfileEvent()
}

sealed class ProfileUiEvent {
    object NavigateToSettings : ProfileUiEvent()
}