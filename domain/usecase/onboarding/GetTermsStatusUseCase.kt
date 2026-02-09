package com.itirafapp.android.domain.usecase.onboarding

import com.itirafapp.android.domain.repository.UserRepository
import javax.inject.Inject

class GetTermsStatusUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Boolean {
        return userRepository.isTermsCompleted()
    }
}