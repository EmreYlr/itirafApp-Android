package com.itirafapp.android.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun getAnonEmail(): String? = prefs.getString("anon_email", null)
    fun saveAnonEmail(email: String) = prefs.edit().putString("anon_email", email).apply()

    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun saveTokens(access: String, refresh: String) {
        prefs.edit().putString("access_token", access).putString("refresh_token", refresh).apply()
    }

    fun deleteTokens() {
        prefs.edit()
            .remove("access_token")
            .remove("refresh_token")
            .apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}