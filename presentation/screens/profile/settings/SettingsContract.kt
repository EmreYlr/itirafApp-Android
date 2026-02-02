package com.itirafapp.android.presentation.screens.profile.settings

import com.itirafapp.android.domain.model.enums.SettingActionType
import com.itirafapp.android.presentation.model.SectionUiModel

data class SettingsState(
    val isLoading: Boolean = false,
    val sections: List<SectionUiModel> = emptyList(),
    val isAnonymous: Boolean = false
)

sealed class SettingsEvent {
    object LogoutClicked : SettingsEvent()
    data class ItemClicked(val action: SettingActionType) : SettingsEvent()
}

sealed class SettingsUiEvent {
    object NavigateToLogin : SettingsUiEvent()
    data class CopyToClipboard(val text: String, val message: String) : SettingsUiEvent()

    //data class NavigateToRoute(val route: String) : SettingsUiEvent()
    data class NavigateToUrl(val url: String) : SettingsUiEvent()
    data class ShowMessage(val message: String) : SettingsUiEvent()
}
