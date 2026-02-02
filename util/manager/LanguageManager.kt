package com.itirafapp.android.util.manager

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor() {

    companion object {
        private const val TAG = "LanguageManager"
    }

    fun getCurrentLanguageCode(): String? {
        val locales = AppCompatDelegate.getApplicationLocales()
        return if (!locales.isEmpty) locales[0]?.language else null
    }

    fun setLanguage(languageCode: String?) {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        val currentLanguageTag = if (!currentLocales.isEmpty) currentLocales[0]?.language else null
        if (currentLanguageTag == languageCode) return

        val appLocale = if (languageCode == null) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(languageCode)
        }

        try {
            AppCompatDelegate.setApplicationLocales(appLocale)
        } catch (e: Exception) {
            Log.w(TAG, "Language change triggered activity recreation with exception", e)
        }
    }
}