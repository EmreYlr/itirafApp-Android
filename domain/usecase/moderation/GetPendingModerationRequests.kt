package com.itirafapp.android.domain.usecase.moderation

import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.ModerationRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPendingModerationRequests @Inject constructor(
    private val repository: ModerationRepository
) {
    operator fun invoke(
        page: Int,
        limit: Int = 10
    ): Flow<Resource<PaginatedResult<ModerationData>>> = flow {
        emit(Resource.Loading)

        try {
            val result = repository.fetchPendingModerationRequests(page = page, limit = limit)

            emit(result)

        } catch (e: Exception) {
            emit(Resource.Error(AppError.LocalError.Unknown))
        }
    }
}