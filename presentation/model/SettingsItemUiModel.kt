package com.itirafapp.android.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.itirafapp.android.domain.model.enums.SettingActionType
import com.itirafapp.android.domain.model.enums.SettingsSection
import com.itirafapp.android.util.state.UiText

data class SettingItemUiModel(
    val title: UiText,
    val icon: ImageVector,
    val type: SettingActionType,
    val isEnabled: Boolean = true,
    val showChevron: Boolean = true,
    val forceClickable: Boolean = false
)

data class SectionUiModel(
    val section: SettingsSection,
    val items: List<SettingItemUiModel>
)