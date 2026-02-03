package com.itirafapp.android.domain.repository

import com.itirafapp.android.util.state.Resource

interface DeviceRepository {
    suspend fun registerOrUpdateDevice(token: String, pushEnabled: Boolean): Resource<Unit>
    fun clearLocalDeviceData()
    fun hasSystemNotificationPermission(): Boolean
}