package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.moderation.ModerationService
import com.itirafapp.android.data.remote.moderation.dto.ModerationDecision
import com.itirafapp.android.data.remote.moderation.dto.ModerationNsfwRequest
import com.itirafapp.android.data.remote.moderation.dto.ModerationRequest
import com.itirafapp.android.data.remote.moderation.dto.ModerationResponse
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.model.enums.Violation
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

    override suspend fun postModerationMessage(
        id: Int,
        decision: ModerationDecision,
        violations: List<Violation>?,
        rejectionReason: String?,
        notes: String?,
        isNsfw: Boolean?
    ): Resource<Unit> {
        return safeApiCall {
            val request = ModerationRequest(decision, violations, rejectionReason, notes, isNsfw)
            api.postModerationMessage(id, request)
        }
    }

    override suspend fun patchModerationNsfw(
        id: Int,
        isNsfw: Boolean
    ): Resource<Unit> {
        return safeApiCall {
            val request = ModerationNsfwRequest(isNsfw)
            api.patchModerationNsfw(id, request)
        }
    }
}