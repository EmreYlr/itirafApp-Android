package com.itirafapp.android.domain.usecase.user

import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {
    operator fun invoke(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val deleteResult = userRepository.deleteAccount()

        when (deleteResult) {
            is Resource.Success -> {
                userRepository.clearUserData()
                followRepository.clearCache()
                emit(Resource.Success(Unit))
            }

            is Resource.Error -> {
                emit(Resource.Error(deleteResult.message ?: "Hesap silerken hata oluştu"))
            }

            else -> Unit
        }
    }
}