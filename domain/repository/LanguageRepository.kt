package com.itirafapp.android.domain.repository

import com.itirafapp.android.util.constant.LanguageConfig

interface LanguageRepository {
    fun getCurrentLanguage(): LanguageConfig
    fun setLanguage(config: LanguageConfig)
}