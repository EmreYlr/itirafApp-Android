package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<Unit>> = flow{
        emit(Resource.Loading())

        val logoutResult = authRepository.logoutUser()

        userRepository.clearUserData()

        if (logoutResult is Resource.Error) {
            emit(Resource.Error(logoutResult.message ?: "Çıkış yapılamadı"))
            return@flow
        }

        emit(Resource.Success(Unit))
    }
}