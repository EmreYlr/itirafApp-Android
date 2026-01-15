package com.itirafapp.android.domain.usecase.user

import com.itirafapp.android.domain.model.User
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())

        val result = repository.getUser()

        emit(result)
    }
}