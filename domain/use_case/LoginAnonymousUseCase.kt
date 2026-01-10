package com.itirafapp.android.domain.use_case

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