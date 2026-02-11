package com.itirafapp.android.domain.usecase.confession

import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject


class ViewConfessionUseCase @Inject constructor(
    private val repository: ConfessionRepository
) {
    suspend operator fun invoke(ids: List<Int>): Resource<Unit> {
        return repository.viewConfession(ids)
    }
}