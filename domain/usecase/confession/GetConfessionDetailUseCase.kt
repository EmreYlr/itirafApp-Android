package com.itirafapp.android.domain.usecase.confession

import com.itirafapp.android.domain.model.ConfessionDetail
import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetConfessionDetailUseCase @Inject constructor(
    private val repository: ConfessionRepository
) {
    operator fun invoke(id: Int): Flow<Resource<ConfessionDetail>> = flow {
        emit(Resource.Loading())

        try {
            val result = repository.getConfessionDetail(id)
            emit(result)

        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Beklenmedik hata"))
        }
    }
}