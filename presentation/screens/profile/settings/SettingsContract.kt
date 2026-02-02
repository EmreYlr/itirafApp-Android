package com.itirafapp.android.presentation.screens.profile.settings

import com.itirafapp.android.domain.model.enums.SettingActionType
import com.itirafapp.android.presentation.model.SectionUiModel
import com.itirafapp.android.util.constant.ThemeConfig

data class SettingsState(
    val isLoading: Boolean = false,
    val sections: List<SectionUiModel> = emptyList(),
    val isAnonymous: Boolean = false,
    val showThemeDialog: Boolean = false,
    val currentTheme: ThemeConfig = ThemeConfig.SYSTEM
)

sealed class SettingsEvent {
    object LogoutClicked : SettingsEvent()
    object OnBackClicked : SettingsEvent()
    data class ItemClicked(val action: SettingActionType) : SettingsEvent()
    object DismissThemeDialog : SettingsEvent()
    data class ThemeSelected(val theme: ThemeConfig) : SettingsEvent()
}

sealed class SettingsUiEvent {
    object NavigateToLogin : SettingsUiEvent()
    object NavigateToBack : SettingsUiEvent()
    object NavigateToEdit : SettingsUiEvent()
    object NavigateToNotification : SettingsUiEvent()
    data class CopyToClipboard(val text: String, val message: String) : SettingsUiEvent()
    data class NavigateToUrl(val url: String) : SettingsUiEvent()
    data class ShowMessage(val message: String) : SettingsUiEvent()
}
