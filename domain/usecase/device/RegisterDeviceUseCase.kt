package com.itirafapp.android.domain.usecase.device

import com.google.firebase.messaging.FirebaseMessaging
import com.itirafapp.android.domain.repository.DeviceRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterDeviceUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(pushEnabled: Boolean): Resource<Unit> {
        return try {
            val token = FirebaseMessaging.getInstance().token.await()

            repository.registerOrUpdateDevice(token, pushEnabled)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Token alınamadı")
        }
    }
}