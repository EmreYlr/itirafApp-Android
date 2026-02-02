package com.itirafapp.android.util.manager

import android.content.Context
import androidx.core.content.edit
import com.itirafapp.android.util.constant.ThemeConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    fun getTheme(): ThemeConfig {
        val themeName = prefs.getString("theme_key", ThemeConfig.SYSTEM.name)
        return try {
            ThemeConfig.valueOf(themeName ?: ThemeConfig.SYSTEM.name)
        } catch (e: Exception) {
            ThemeConfig.SYSTEM
        }
    }

    fun saveTheme(config: ThemeConfig) {
        prefs.edit {
            putString("theme_key", config.name)
        }
    }
}