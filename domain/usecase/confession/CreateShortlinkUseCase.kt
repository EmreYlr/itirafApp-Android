package com.itirafapp.android.domain.usecase.confession

import com.itirafapp.android.data.remote.confession.dto.ShortlinkResponse
import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateShortlinkUseCase @Inject constructor(
    private val confessionRepository: ConfessionRepository
) {
    operator fun invoke(id: Int): Flow<Resource<ShortlinkResponse>> = flow {
        emit(Resource.Loading)

        val result = confessionRepository.createShortlink(id)

        emit(result)
    }
}