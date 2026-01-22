package com.itirafapp.android.util.manager

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.itirafapp.android.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserManager @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        val json = gson.toJson(user)
        prefs.edit { putString("current_user", json) }
    }

    fun getUser(): User? {
        val json = prefs.getString("current_user", null) ?: return null
        return gson.fromJson(json, User::class.java)
    }

    fun deleteUser() {
        prefs.edit { remove("current_user") }
    }

    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean("is_onboarding_completed", false)
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit { putBoolean("is_onboarding_completed", completed) }
    }
}