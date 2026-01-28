package com.itirafapp.android.domain.usecase.confession

import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostConfessionUseCase @Inject constructor(
    private val repository: ConfessionRepository
) {
    operator fun invoke(
        channelId: Int,
        title: String?,
        message: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val result = repository.postConfession(channelId, title, message)

        emit(result)
    }
}