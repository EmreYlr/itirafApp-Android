package com.itirafapp.android.util.state

import android.content.Context
import android.content.Intent
import com.itirafapp.android.R

fun shareLink(context: Context, link: String) {
    val title = context.getString(R.string.share_confession_title)

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, link)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, title)
    context.startActivity(shareIntent)
}