package com.itirafapp.android.domain.model.enums

import androidx.annotation.StringRes
import com.itirafapp.android.R

enum class SettingActionType {
    EDIT_PROFILE,
    THEME,
    NOTIFICATIONS,
    LANGUAGE,
    RULES,
    PRIVACY_POLICY,
    USER_AGREEMENT,
    HELP_CENTER,
    CONTACT_INFO
}

enum class SettingsSection(@param:StringRes val titleRes: Int) {
    PROFILE(R.string.profile),
    GENERAL(R.string.general),
    ABOUT(R.string.about),
    SUPPORT(R.string.support)
}