package com.itirafapp.android.util.extension

import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp

fun Modifier.compatibleBlur(
    radius: Dp,
    fallbackAlpha: Float = 0f
): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Android 12+ (API 31+): Use native blur
        this.blur(radius)
    } else {
        // Android 8-11 (API 26-30): Use alpha reduction as fallback
        this.alpha(fallbackAlpha)
    }
}
