package com.itirafapp.android.domain.usecase.room

import com.itirafapp.android.domain.repository.RoomRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteSentMessageRequestUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    operator fun invoke(requestId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val result = repository.deleteSentMessageRequest(requestId)
        emit(result)
    }
}