package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.confession.ConfessionService
import com.itirafapp.android.data.remote.confession.dto.ConfessionResponse
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.domain.model.ConfessionData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.util.Resource
import com.itirafapp.android.util.map
import javax.inject.Inject

class ConfessionRepositoryImpl @Inject constructor(
    private val api: ConfessionService
) : ConfessionRepository {
    override suspend fun getConfessions(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ConfessionData>> {
        val apiResult: Resource<ConfessionResponse> = safeApiCall {
            api.fetchFlow(page = page, limit = limit)
        }

        return apiResult.map { response ->
            response.toDomain()
        }
    }

    override suspend fun likeConfession(id: Int): Resource<Unit> {
        return safeApiCall {
            api.likeConfession(id)
        }
    }

    override suspend fun unlikeConfession(id: Int): Resource<Unit> {
        return safeApiCall {
            api.unlikeConfession(id)
        }
    }
}