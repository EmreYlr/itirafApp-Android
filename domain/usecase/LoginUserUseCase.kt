package com.itirafapp.android.domain.usecase

import com.itirafapp.android.data.remote.dto.LoginRequest
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.Resource
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: LoginRequest): Resource<Unit> {
        return repository.loginUser(request)
    }
}