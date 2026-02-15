package com.itirafapp.android.domain.usecase.moderation

import com.itirafapp.android.data.remote.moderation.dto.ModerationDecision
import com.itirafapp.android.domain.model.enums.Violation
import com.itirafapp.android.domain.repository.ModerationRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostModerationMessageUseCase @Inject constructor(
    private val repository: ModerationRepository
) {
    operator fun invoke(
        id: Int,
        decision: ModerationDecision,
        violations: List<Violation>? = null,
        rejectionReason: String? = null,
        notes: String? = null,
        isNsfw: Boolean? = null
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)

        val result = repository.postModerationMessage(
            id = id,
            decision = decision,
            violations = violations,
            rejectionReason = rejectionReason,
            notes = notes,
            isNsfw = isNsfw
        )

        emit(result)
    }
}