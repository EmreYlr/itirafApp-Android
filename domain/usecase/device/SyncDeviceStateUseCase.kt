package com.itirafapp.android.domain.usecase.device

import com.itirafapp.android.domain.repository.DeviceRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class SyncDeviceStateUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(isSystemPermissionGranted: Boolean): Resource<Unit> {
        return repository.syncDeviceState(isSystemPermissionGranted)
    }
}