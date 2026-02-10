package com.itirafapp.android.domain.usecase.confession

import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.domain.model.ConfessionData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFollowingConfessionsUseCase @Inject constructor(
    private val repository: ConfessionRepository
) {
    operator fun invoke(
        page: Int,
        limit: Int = 10
    ): Flow<Resource<PaginatedResult<ConfessionData>>> = flow {
        emit(Resource.Loading)

        try {
            val result = repository.getFollowingConfessions(page, limit)

            emit(result)

        } catch (e: Exception) {
            emit(Resource.Error(AppError.LocalError.Unknown))
        }
    }
}