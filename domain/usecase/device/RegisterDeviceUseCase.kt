package com.itirafapp.android.domain.usecase.device

import com.itirafapp.android.domain.repository.DeviceRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class RegisterDeviceUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(pushEnabled: Boolean): Resource<Unit> {
        return try {
            repository.registerOrUpdateDevice(pushEnabled)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Token alınamadı")
        }
    }
}