package com.itirafapp.android.domain.usecase.confession

import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.util.Resource
import javax.inject.Inject

class LikeConfessionUseCase @Inject constructor(
    private val repository: ConfessionRepository
) {
    suspend operator fun invoke(id: Int): Resource<Unit> {
        return repository.likeConfession(id)
    }
}