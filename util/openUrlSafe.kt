package com.itirafapp.android.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

fun openUrlSafe(context: Context, url: String, colorParams: Int) {
    val uri = Uri.parse(url)

    try {
        val builder = CustomTabsIntent.Builder()
        val colorScheme = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorParams)
            .build()
        builder.setDefaultColorSchemeParams(colorScheme)
        builder.setShowTitle(true)

        builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        val customTabsIntent = builder.build()

        customTabsIntent.launchUrl(context, uri)

    } catch (e: ActivityNotFoundException) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(browserIntent)
        } catch (e2: Exception) {
            Toast.makeText(context, "Bağlantıyı açacak bir tarayıcı bulunamadı.", Toast.LENGTH_LONG).show()
        }
    }
}