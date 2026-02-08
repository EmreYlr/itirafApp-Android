package com.itirafapp.android.util.extension

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatToRelativeTime(isoString: String?): String {
    if (isoString.isNullOrEmpty()) return ""

    return try {
        val time = Instant.parse(isoString)
        val now = Instant.now()
        val duration = Duration.between(time, now)

        val days = duration.toDays()
        val hours = duration.toHours()
        val minutes = duration.toMinutes()

        val isTurkish = Locale.getDefault().language == "tr"

        when {
            days > 0 -> if (isTurkish) "$days gün önce" else "$days days ago"
            hours > 0 -> if (isTurkish) "$hours sa önce" else "$hours hours ago"
            minutes > 0 -> if (isTurkish) "$minutes dk önce" else "$minutes minutes ago"
            else -> if (isTurkish) "Az önce" else "Just now"
        }

    } catch (e: Exception) {
        isoString
    }
}

fun parseToLocalDate(isoString: String?): LocalDate? {
    if (isoString.isNullOrEmpty()) return null
    return try {
        Instant.parse(isoString)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } catch (e: Exception) {
        null
    }
}

fun formatDateSeparator(isoString: String?): String {
    if (isoString.isNullOrEmpty()) return ""

    return try {
        val time = Instant.parse(isoString)
        val messageDate = time.atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()
        val daysDifference = java.time.temporal.ChronoUnit.DAYS.between(messageDate, today)

        val isTurkish = Locale.getDefault().language == "tr"

        when (daysDifference) {
            0L -> if (isTurkish) "Bugün" else "Today"
            1L -> if (isTurkish) "Dün" else "Yesterday"
            in 2..6 -> if (isTurkish) "$daysDifference gün önce" else "$daysDifference days ago"
            else -> {
                val pattern = if (isTurkish) "d MMMM yyyy" else "MMMM d, yyyy"
                val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                messageDate.format(formatter)
            }
        }
    } catch (e: Exception) {
        ""
    }
}

fun formatToTime(isoString: String?): String {
    if (isoString.isNullOrEmpty()) return ""

    return try {
        val time = Instant.parse(isoString)
        val localTime = time.atZone(ZoneId.systemDefault()).toLocalTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        localTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
}