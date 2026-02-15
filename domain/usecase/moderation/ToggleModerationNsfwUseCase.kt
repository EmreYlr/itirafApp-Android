package com.itirafapp.android.domain.usecase.moderation

import com.itirafapp.android.domain.repository.ModerationRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToggleModerationNsfwUseCase @Inject constructor(
    private val repository: ModerationRepository
) {
    operator fun invoke(
        id: Int,
        isNsfw: Boolean
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)

        val result = repository.patchModerationNsfw(
            id = id,
            isNsfw = isNsfw
        )

        emit(result)
    }
}