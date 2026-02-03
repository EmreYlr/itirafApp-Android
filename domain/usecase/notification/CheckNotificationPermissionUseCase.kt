package com.itirafapp.android.domain.usecase.notification

import com.itirafapp.android.domain.repository.DeviceRepository
import javax.inject.Inject

class CheckNotificationPermissionUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    operator fun invoke(): Boolean {
        return repository.hasSystemNotificationPermission()
    }
}