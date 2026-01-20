package com.itirafapp.android.domain.usecase.onboarding

import com.itirafapp.android.domain.repository.UserRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() {
        userRepository.setOnboardingCompleted(true)
    }
}