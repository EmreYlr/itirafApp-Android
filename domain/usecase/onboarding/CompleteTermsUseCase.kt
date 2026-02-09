package com.itirafapp.android.domain.usecase.onboarding

import com.itirafapp.android.domain.repository.UserRepository
import javax.inject.Inject

class CompleteTermsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.setTermsCompleted(true)
    }
}