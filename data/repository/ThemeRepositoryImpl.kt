package com.itirafapp.android.data.repository

import com.itirafapp.android.domain.repository.ThemeRepository
import com.itirafapp.android.util.constant.ThemeConfig
import com.itirafapp.android.util.manager.ThemeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    private val themeManager: ThemeManager
) : ThemeRepository {

    private val _themeFlow = MutableStateFlow(themeManager.getTheme())

    override fun getAppTheme(): Flow<ThemeConfig> {
        return _themeFlow.asStateFlow()
    }

    override suspend fun setAppTheme(theme: ThemeConfig) {
        themeManager.saveTheme(theme)
        _themeFlow.emit(theme)
    }
}