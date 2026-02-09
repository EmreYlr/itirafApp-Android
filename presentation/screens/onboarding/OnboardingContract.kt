package com.itirafapp.android.presentation.screens.onboarding

import com.itirafapp.android.presentation.model.OnboardingPage

data class OnboardingState(
    val currentPageIndex: Int = 0,
    val pages: List<OnboardingPage> = emptyList()
)

sealed class OnboardingEvent {
    data class UpdatePage(val index: Int) : OnboardingEvent()
    object ClickNext : OnboardingEvent()
    object ClickBack : OnboardingEvent()
}

sealed class OnboardingUiEvent {
    object NavigateToTerms : OnboardingUiEvent()
}