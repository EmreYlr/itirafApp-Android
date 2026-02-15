package com.itirafapp.android.util.extension

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun formatToRelativeTime(isoString: String?): String {
    if (isoString.isNullOrEmpty()) return ""

    return try {
        val localDateTime = LocalDateTime.parse(isoString.substringBefore("Z"))
        val time = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
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
        LocalDateTime.parse(isoString.substringBefore("Z")).toLocalDate()
    } catch (e: Exception) {
        null
    }
}

fun formatDateSeparator(isoString: String?): String {
    if (isoString.isNullOrEmpty()) return ""

    return try {
        val localDateTime = LocalDateTime.parse(isoString.substringBefore("Z"))
        val messageDate = localDateTime.toLocalDate()
        val today = LocalDate.now()

        val daysDifference = ChronoUnit.DAYS.between(messageDate, today)
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
        val localDateTime = LocalDateTime.parse(isoString.substringBefore("Z"))

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        localDateTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
}

fun formatToDateTime(isoString: String?): String {
    if (isoString.isNullOrEmpty()) return ""

    return try {
        val localDateTime = LocalDateTime.parse(isoString.substringBefore("Z"))

        val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
        localDateTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
}