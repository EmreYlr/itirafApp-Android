package com.itirafapp.android.data.repository

import com.itirafapp.android.data.remote.device.DeviceService
import com.itirafapp.android.data.remote.device.dto.DeviceRegisterRequest
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.domain.repository.DeviceRepository
import com.itirafapp.android.util.manager.DeviceManager
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val api: DeviceService,
    private val deviceManager: DeviceManager
) : DeviceRepository {

    override suspend fun registerOrUpdateDevice(
        token: String,
        pushEnabled: Boolean
    ): Resource<Unit> {
        return try {
            val savedToken = deviceManager.getSavedToken()
            val savedPushEnabled = deviceManager.getSavedPushEnabled()

            if (token == savedToken && pushEnabled == savedPushEnabled) {
                return Resource.Success(Unit)
            }

            val request = DeviceRegisterRequest(
                token = token,
                platform = deviceManager.getPlatform(),
                appVersion = deviceManager.getAppVersion(),
                deviceModel = deviceManager.getDeviceModel(),
                osVersion = deviceManager.getOsVersion(),
                pushEnabled = pushEnabled
            )

            val response = safeApiCall { api.registerDevice(request) }

            if (response is Resource.Success) {
                deviceManager.saveDeviceState(token, pushEnabled)
            }

            response

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Beklenmeyen hata")
        }
    }

    override fun clearLocalDeviceData() {
        deviceManager.clearDeviceState()
    }

    override fun hasSystemNotificationPermission(): Boolean {
        return deviceManager.isNotificationPermissionGranted()
    }
}