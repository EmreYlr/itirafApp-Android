package com.itirafapp.android.domain.usecase

import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.Resource
import javax.inject.Inject

class LoginAnonymousUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.loginAnonymous()
    }
}