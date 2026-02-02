package com.itirafapp.android.domain.repository

import com.itirafapp.android.util.constant.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getAppTheme(): Flow<ThemeConfig>
    suspend fun setAppTheme(theme: ThemeConfig)
}