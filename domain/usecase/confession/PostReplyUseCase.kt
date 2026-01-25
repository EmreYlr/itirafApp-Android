package com.itirafapp.android.domain.usecase.confession

import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostReplyUseCase @Inject constructor(
    private val confessionRepository: ConfessionRepository
) {
    operator fun invoke(id: Int, message: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val result = confessionRepository.postReply(id, message)

        emit(result)
    }
}