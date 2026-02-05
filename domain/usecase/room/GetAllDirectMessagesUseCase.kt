package com.itirafapp.android.domain.usecase.room

import com.itirafapp.android.domain.model.DirectMessage
import com.itirafapp.android.domain.repository.RoomRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllDirectMessagesUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    operator fun invoke(): Flow<Resource<List<DirectMessage>>> = flow {
        emit(Resource.Loading())

        val result = repository.getAllDirectMessages()

        emit(result)
    }

}