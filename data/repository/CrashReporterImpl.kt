package com.itirafapp.android.data.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.itirafapp.android.domain.repository.CrashReporter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashReporterImpl @Inject constructor() : CrashReporter {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    override fun logMessage(message: String) {
        crashlytics.log(message)
    }

    override fun logNonFatal(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }

    override fun setUserAnonymous(isAnonymous: Boolean) {
        crashlytics.setCustomKey("is_anonymous_user", isAnonymous)
    }

    override fun setClaritySessionLink(url: String) {
        crashlytics.setCustomKey("claritySessionLink", url)
    }

    override fun sendNetworkError(
        throwable: Throwable,
        endpoint: String,
        method: String,
        statusCode: Int
    ) {
        crashlytics.setCustomKey("network_endpoint", endpoint)
        crashlytics.setCustomKey("network_method", method)
        crashlytics.setCustomKey("network_status_code", statusCode)

        crashlytics.recordException(throwable)
    }
}