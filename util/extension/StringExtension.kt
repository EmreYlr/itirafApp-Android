package com.itirafapp.android.util.extension

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun String.toTruncatedAnnotatedString(
    limit: Int = 300,
    seeMoreColor: Color,
    seeMoreText: String
): AnnotatedString {
    return if (this.length > limit) {
        buildAnnotatedString {
            append(this@toTruncatedAnnotatedString.take(limit))
            append("... ")

            withStyle(
                style = SpanStyle(
                    color = seeMoreColor,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(seeMoreText)
            }
        }
    } else {
        buildAnnotatedString { append(this@toTruncatedAnnotatedString) }
    }
}