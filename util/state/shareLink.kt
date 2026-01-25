package com.itirafapp.android.util.state

import android.content.Context
import android.content.Intent

fun shareLink(context: Context, link: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, link)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "İtirafı Paylaş")
    context.startActivity(shareIntent)
}