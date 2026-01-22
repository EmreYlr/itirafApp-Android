package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.ConfessionData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.util.state.Resource

interface ConfessionRepository {
    suspend fun getConfessions(page: Int, limit: Int): Resource<PaginatedResult<ConfessionData>>
    suspend fun getFollowingConfessions(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ConfessionData>>
    suspend fun likeConfession(id: Int): Resource<Unit>
    suspend fun unlikeConfession(id: Int): Resource<Unit>
}