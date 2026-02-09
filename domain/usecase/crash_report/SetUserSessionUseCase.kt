package com.itirafapp.android.domain.usecase.crash_report

import com.itirafapp.android.domain.repository.CrashReporter
import com.itirafapp.android.domain.repository.SessionTracker
import javax.inject.Inject

class SetUserSessionUseCase @Inject constructor(
    private val crashReporter: CrashReporter,
    private val sessionTracker: SessionTracker
) {
    operator fun invoke(userId: String?) {
        if (!userId.isNullOrBlank()) {
            sessionTracker.setUserId(userId)
            crashReporter.setUserId(userId)
            crashReporter.setUserAnonymous(false)
            crashReporter.logMessage("Session started for user: $userId")
        } else {
            sessionTracker.clearUser()
            crashReporter.setUserId("")
            crashReporter.setUserAnonymous(true)
            crashReporter.logMessage("Session started: Anonymous")
        }
    }
}