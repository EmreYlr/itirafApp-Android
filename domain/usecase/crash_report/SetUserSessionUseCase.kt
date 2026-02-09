package com.itirafapp.android.domain.usecase.crash_report

import com.itirafapp.android.domain.repository.CrashReporter
import javax.inject.Inject

class SetUserSessionUseCase @Inject constructor(
    private val crashReporter: CrashReporter
) {
    operator fun invoke(userId: String?) {
        if (!userId.isNullOrBlank()) {
            crashReporter.setUserId(userId)
            crashReporter.setUserAnonymous(false)
            crashReporter.logMessage("Session started for user: $userId")
        } else {
            crashReporter.setUserId("")
            crashReporter.setUserAnonymous(true)
            crashReporter.logMessage("Session started: Anonymous")
        }
    }
}