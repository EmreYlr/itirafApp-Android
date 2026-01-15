package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.data.remote.auth.dto.ResetPasswordRequest
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
)  {
    operator fun invoke(request: ResetPasswordRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val result = authRepository.resetPassword(request)

        emit(result)
    }
}