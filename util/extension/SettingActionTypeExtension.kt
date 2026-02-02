package com.itirafapp.android.util.extension

import com.itirafapp.android.domain.model.enums.SettingActionType
import com.itirafapp.android.util.constant.Constants

fun SettingActionType.getUrl(): String? {
    return when (this) {
        SettingActionType.RULES -> Constants.RULES_URL
        SettingActionType.PRIVACY_POLICY -> Constants.PRIVACY_URL
        SettingActionType.USER_AGREEMENT -> Constants.TERMS_URL
        else -> null
    }
}