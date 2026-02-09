package com.itirafapp.android.domain.usecase.crash_report

import com.itirafapp.android.domain.repository.SessionTracker
import javax.inject.Inject

class SetupTrackingUseCase @Inject constructor(
    private val sessionTracker: SessionTracker
) {
    operator fun invoke() {
        sessionTracker.setup()
    }
}