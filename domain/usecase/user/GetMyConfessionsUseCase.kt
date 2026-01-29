package com.itirafapp.android.domain.usecase.user

import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMyConfessionsUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(
        page: Int,
        limit: Int = 10
    ): Flow<Resource<PaginatedResult<MyConfessionData>>> = flow {

        emit(Resource.Loading())

        val result = repository.getMyConfessions(page, limit)

        emit(result)
    }
}