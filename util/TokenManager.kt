package com.itirafapp.android.util

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun saveTokens(access: String, refresh: String) {
        prefs.edit {
            putString("access_token", access)
                .putString("refresh_token", refresh)
        }
    }

    fun deleteTokens() {
        prefs.edit { clear() }
    }
}