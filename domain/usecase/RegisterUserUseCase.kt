package com.itirafapp.android.domain.usecase

import com.itirafapp.android.data.remote.dto.RegisterRequest
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.Resource
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: RegisterRequest): Resource<Unit> {
        return repository.registerUser(request)
    }
}