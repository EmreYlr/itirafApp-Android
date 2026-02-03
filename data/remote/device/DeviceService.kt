package com.itirafapp.android.data.remote.device

import com.itirafapp.android.data.remote.device.dto.DeviceRegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DeviceService {
    @POST("devices")
    suspend fun registerDevice(
        @Body request: DeviceRegisterRequest
    ): Unit
}