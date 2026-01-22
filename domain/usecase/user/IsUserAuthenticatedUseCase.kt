package com.itirafapp.android.domain.usecase.user


import com.itirafapp.android.domain.repository.UserRepository
import javax.inject.Inject

class IsUserAuthenticatedUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Boolean {
        return userRepository.isUserAuthenticated()
    }
}