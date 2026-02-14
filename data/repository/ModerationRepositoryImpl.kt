package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.moderation.ModerationService
import com.itirafapp.android.data.remote.moderation.dto.ModerationResponse
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.ModerationRepository
import com.itirafapp.android.util.state.Resource
import com.itirafapp.android.util.state.map
import javax.inject.Inject

class ModerationRepositoryImpl @Inject constructor(
    val api: ModerationService
) : ModerationRepository {
    override suspend fun fetchPendingModerationRequests(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ModerationData>> {
        val apiResult: Resource<ModerationResponse> = safeApiCall {
            api.fetchPendingModerationRequests(page = page, limit = limit)
        }

        return apiResult.map { response ->
            response.toDomain()
        }
    }

}