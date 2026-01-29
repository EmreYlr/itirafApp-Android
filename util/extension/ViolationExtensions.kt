package com.itirafapp.android.util.extension

import android.content.Context
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.enums.Violation

fun Violation.getLabel(context: Context): String {
    val resId = when (this) {
        Violation.NONE -> R.string.violation_none
        Violation.PROFANITY -> R.string.violation_profanity
        Violation.HARASSMENT -> R.string.violation_harassment
        Violation.PERSONAL_INFO -> R.string.violation_personal_info
        Violation.HATE_SPEECH -> R.string.violation_hate_speech
        Violation.THREAT -> R.string.violation_threat
        Violation.SEXUAL_CONTENT -> R.string.violation_sexual_content
        Violation.VIOLENCE -> R.string.violation_violence
        Violation.DISCRIMINATION -> R.string.violation_discrimination
        Violation.SPAM -> R.string.violation_spam
        Violation.OTHER -> R.string.violation_other
    }
    return context.getString(resId)
}