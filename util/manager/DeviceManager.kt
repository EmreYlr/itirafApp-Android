package com.itirafapp.android.util.manager

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("device_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DEVICE_TOKEN = "device_token"
        private const val KEY_PUSH_ENABLED = "push_enabled"
    }


    fun getAppVersion(): String {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    fun getOsVersion(): String = Build.VERSION.RELEASE ?: "Unknown"

    fun getDeviceModel(): String {
        val manufacturer = Build.MANUFACTURER ?: ""
        val model = Build.MODEL ?: ""
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }

    fun getPlatform(): String = "ANDROID"

    private fun capitalize(s: String?): String {
        if (s.isNullOrEmpty()) return ""
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    fun isNotificationPermissionGranted(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun getSavedToken(): String? {
        return prefs.getString(KEY_DEVICE_TOKEN, null)
    }

    fun getSavedPushEnabled(): Boolean {
        return prefs.getBoolean(KEY_PUSH_ENABLED, true)
    }

    fun saveDeviceState(token: String, isEnabled: Boolean) {
        prefs.edit().apply {
            putString(KEY_DEVICE_TOKEN, token)
            putBoolean(KEY_PUSH_ENABLED, isEnabled)
            apply()
        }
    }

    fun clearDeviceState() {
        prefs.edit().apply {
            remove(KEY_DEVICE_TOKEN)
            remove(KEY_PUSH_ENABLED)
            apply()
        }
    }
}