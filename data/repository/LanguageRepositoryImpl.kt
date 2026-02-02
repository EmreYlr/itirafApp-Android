package com.itirafapp.android.data.repository

import com.itirafapp.android.domain.repository.LanguageRepository
import com.itirafapp.android.util.constant.LanguageConfig
import com.itirafapp.android.util.manager.LanguageManager
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val languageManager: LanguageManager
) : LanguageRepository {

    override fun getCurrentLanguage(): LanguageConfig {
        val code = languageManager.getCurrentLanguageCode()
        return when (code) {
            "tr" -> LanguageConfig.TR
            "en" -> LanguageConfig.EN
            else -> LanguageConfig.SYSTEM
        }
    }

    override fun setLanguage(config: LanguageConfig) {
        val code = when (config) {
            LanguageConfig.TR -> "tr"
            LanguageConfig.EN -> "en"
            LanguageConfig.SYSTEM -> null
        }
        languageManager.setLanguage(code)
    }
}