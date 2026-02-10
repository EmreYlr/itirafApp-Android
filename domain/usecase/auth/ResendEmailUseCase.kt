package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResendEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String): Flow<Resource<Unit>> = flow {

        emit(Resource.Loading)

        val result = authRepository.resendEmail(email)

        emit(result)
    }
}