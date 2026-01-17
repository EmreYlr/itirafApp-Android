package com.itirafapp.android.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun openUrlSafe(context: Context, url: String, colorParams: Int) {
    val uri = url.toUri()

    try {
        val builder = CustomTabsIntent.Builder()
        val colorScheme = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorParams)
            .build()
        builder.setDefaultColorSchemeParams(colorScheme)
        builder.setShowTitle(true)

        builder.setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)

        val customTabsIntent = builder.build()

        customTabsIntent.launchUrl(context, uri)

    } catch (_: ActivityNotFoundException) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(browserIntent)
        } catch (_: Exception) {
            Toast.makeText(context, "Bağlantıyı açacak bir tarayıcı bulunamadı.", Toast.LENGTH_LONG).show()
        }
    }
}