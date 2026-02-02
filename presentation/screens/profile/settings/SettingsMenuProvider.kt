package com.itirafapp.android.presentation.screens.profile.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Security
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.enums.SettingActionType
import com.itirafapp.android.domain.model.enums.SettingsSection
import com.itirafapp.android.presentation.model.SectionUiModel
import com.itirafapp.android.presentation.model.SettingItemUiModel
import com.itirafapp.android.util.constant.Constants
import com.itirafapp.android.util.state.UiText
import javax.inject.Inject

class SettingsMenuProvider @Inject constructor() {
    fun getMenu(isAnonymous: Boolean): List<SectionUiModel> {
        val menu = mutableListOf<SectionUiModel>()

        // 1. PROFILE
        val profileItems = listOf(
            SettingItemUiModel(
                title = UiText.StringResource(R.string.edit_profile),
                icon = Icons.Outlined.Person,
                type = SettingActionType.EDIT_PROFILE,
                isEnabled = !isAnonymous
            )
        )
        menu.add(SectionUiModel(SettingsSection.PROFILE, profileItems))

        // 2. GENERAL
        val generalItems = listOf(
            SettingItemUiModel(
                title = UiText.StringResource(R.string.theme),
                icon = Icons.Outlined.DarkMode,
                type = SettingActionType.THEME
            ),
            SettingItemUiModel(
                title = UiText.StringResource(R.string.notifications),
                icon = Icons.Outlined.Notifications,
                type = SettingActionType.NOTIFICATIONS
            ),
            SettingItemUiModel(
                title = UiText.StringResource(R.string.language),
                icon = Icons.Outlined.Language,
                type = SettingActionType.LANGUAGE
            )
        )
        menu.add(SectionUiModel(SettingsSection.GENERAL, generalItems))

        // 3. ABOUT
        val aboutItems = listOf(
            SettingItemUiModel(
                title = UiText.StringResource(R.string.rules),
                icon = Icons.AutoMirrored.Outlined.List,
                type = SettingActionType.RULES
            ),
            SettingItemUiModel(
                title = UiText.StringResource(R.string.privacy_policy),
                icon = Icons.Outlined.Security,
                type = SettingActionType.PRIVACY_POLICY
            ),
            SettingItemUiModel(
                title = UiText.StringResource(R.string.user_agreement),
                icon = Icons.Outlined.Description,
                type = SettingActionType.USER_AGREEMENT
            )
        )
        menu.add(SectionUiModel(SettingsSection.ABOUT, aboutItems))

        // 4. SUPPORT
        val supportItems = listOf(
            SettingItemUiModel(
                title = UiText.StringResource(R.string.help_center),
                icon = Icons.AutoMirrored.Outlined.Help,
                type = SettingActionType.HELP_CENTER
            ),
            SettingItemUiModel(
                title = UiText.DynamicString(Constants.INFO_MAIL),
                icon = Icons.Outlined.Email,
                type = SettingActionType.CONTACT_INFO,
                isEnabled = false,
                showChevron = false,
                forceClickable = true
            )
        )
        menu.add(SectionUiModel(SettingsSection.SUPPORT, supportItems))

        return menu
    }
}