package com.itirafapp.android.domain.usecase.user

import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BlockUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(
        targetUserId: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val result = repository.blockUser(
            targetUserId = targetUserId
        )

        emit(result)
    }
}