package com.itirafapp.android.domain.usecase.room

import com.itirafapp.android.domain.repository.RoomRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReportRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    operator fun invoke(id: String, reason: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)

        val result = repository.reportRoom(id, reason)

        emit(result)
    }
}
