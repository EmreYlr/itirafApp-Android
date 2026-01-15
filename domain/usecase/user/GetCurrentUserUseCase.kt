package com.itirafapp.android.domain.usecase.user

import com.itirafapp.android.domain.model.User
import com.itirafapp.android.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): User? {
        return repository.getLocalUser()
    }
}