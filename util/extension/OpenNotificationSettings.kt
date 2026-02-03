package com.itirafapp.android.util.extension

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

fun Context.openNotificationSettings() {
    try {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(this, "Ayarlar açılamadı", Toast.LENGTH_SHORT).show()
    }
}