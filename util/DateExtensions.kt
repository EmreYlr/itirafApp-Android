package com.itirafapp.android.util

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatToRelativeTime(isoString: String?): String {
    if (isoString.isNullOrEmpty()) return ""

    return try {
        val time = Instant.parse(isoString)
        val now = Instant.now()
        val duration = Duration.between(time, now)
        val seconds = duration.seconds

        when {
            seconds < 60 -> "Az önce"
            seconds < 3600 -> "${seconds / 60} dk önce"
            seconds < 86400 -> "${seconds / 3600} sa önce"
            seconds < 604800 -> "${seconds / 86400} gün önce"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("tr"))
                    .withZone(ZoneId.systemDefault())
                formatter.format(time)
            }
        }
    } catch (e: Exception) {
        isoString ?: ""
    }
}