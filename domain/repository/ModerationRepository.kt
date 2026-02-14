package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.util.state.Resource

interface ModerationRepository {
    suspend fun fetchPendingModerationRequests(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ModerationData>>
}