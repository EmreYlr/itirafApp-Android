package com.itirafapp.android.data.repository

import android.content.Context
import com.itirafapp.android.BuildConfig
import com.itirafapp.android.domain.repository.CrashReporter
import com.itirafapp.android.domain.repository.SessionTracker
import com.microsoft.clarity.Clarity
import com.microsoft.clarity.ClarityConfig
import com.microsoft.clarity.models.LogLevel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClaritySessionTracker @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val crashReporter: CrashReporter
) : SessionTracker {

    override fun setup() {
        val projectId = BuildConfig.CLARITY_PROJECT_ID

        if (projectId.isBlank()) {
            return
        }
        val logLevel = if (BuildConfig.DEBUG) LogLevel.Verbose else LogLevel.None

        val config = ClarityConfig(
            projectId = projectId,
            logLevel = logLevel
        )

        val isInitialized = Clarity.initialize(context, config)

        if (isInitialized) {
            updateCrashlyticsSessionLink()
        }
    }

    override fun setUserId(userId: String) {
        Clarity.setCustomUserId(userId)
        updateCrashlyticsSessionLink()
    }

    override fun clearUser() {
        Clarity.setCustomUserId(null)
    }

    override fun setCurrentScreen(screenName: String) {
        Clarity.setCurrentScreenName(screenName)
    }

    private fun updateCrashlyticsSessionLink() {
        val sessionUrl = Clarity.getCurrentSessionUrl()

        if (!sessionUrl.isNullOrBlank()) {
            crashReporter.setClaritySessionLink(sessionUrl)
        }
    }
}