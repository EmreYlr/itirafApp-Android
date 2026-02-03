package com.itirafapp.android.domain.repository

import com.itirafapp.android.util.state.Resource

interface DeviceRepository {
    suspend fun registerOrUpdateDevice(pushEnabled: Boolean): Resource<Unit>
    suspend fun syncDeviceState(isSystemPermissionGranted: Boolean): Resource<Unit>
    fun clearLocalDeviceData()
    fun hasSystemNotificationPermission(): Boolean

}