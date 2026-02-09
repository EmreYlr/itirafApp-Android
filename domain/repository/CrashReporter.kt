package com.itirafapp.android.domain.repository


interface CrashReporter {
    fun setUserId(userId: String)
    fun logMessage(message: String)
    fun logNonFatal(throwable: Throwable)

    fun setCustomKey(key: String, value: String)
    fun setCustomKey(key: String, value: Boolean)
    fun setCustomKey(key: String, value: Int)

    fun setUserAnonymous(isAnonymous: Boolean)
    fun setClaritySessionLink(url: String)
    fun sendNetworkError(throwable: Throwable, endpoint: String, method: String, statusCode: Int)
}