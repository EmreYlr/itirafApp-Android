package com.itirafapp.android.domain.repository

import com.itirafapp.android.data.remote.moderation.dto.ModerationDecision
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.model.enums.Violation
import com.itirafapp.android.util.state.Resource

interface ModerationRepository {
    suspend fun fetchPendingModerationRequests(
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<ModerationData>>

    suspend fun postModerationMessage(
        id: Int,
        decision: ModerationDecision,
        violations: List<Violation>?,
        rejectionReason: String?,
        notes: String?,
        isNsfw: Boolean?
    ): Resource<Unit>

    suspend fun patchModerationNsfw(
        id: Int,
        isNsfw: Boolean
    ): Resource<Unit>
}