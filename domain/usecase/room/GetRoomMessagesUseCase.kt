package com.itirafapp.android.domain.usecase.room

import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.RoomRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRoomMessagesUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    operator fun invoke(
        roomId: String,
        page: Int,
        limit: Int = 10
    ): Flow<Resource<PaginatedResult<MessageData>>> = flow {
        emit(Resource.Loading())

        try {
            val result = repository.getRoomMessages(roomId, page = page, limit = limit)

            emit(result)

        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Beklenmedik hata"))
        }
    }
}