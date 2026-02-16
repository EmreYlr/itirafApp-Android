package com.itirafapp.android.domain.usecase.user

import com.itirafapp.android.domain.model.BlockedUsers
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBlockedUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Resource<List<BlockedUsers>>> = flow {
        emit(Resource.Loading)

        val result = repository.getBlockedUsers()

        emit(result)
    }
}