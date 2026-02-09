package com.itirafapp.android.presentation.screens.onboarding

import com.itirafapp.android.R
import com.itirafapp.android.presentation.model.OnboardingPage
import com.itirafapp.android.util.state.UiText
import javax.inject.Inject

class OnboardingProvider @Inject constructor() {

    fun getOnboardingPages(): List<OnboardingPage> {
        return listOf(
            OnboardingPage(
                title = UiText.StringResource(R.string.onboarding_slide1_title),
                description = UiText.StringResource(R.string.onboarding_slide1_desc),
                imageRes = R.drawable.ic_onboarding_1
            ),
            OnboardingPage(
                title = UiText.StringResource(R.string.onboarding_slide2_title),
                description = UiText.StringResource(R.string.onboarding_slide2_desc),
                imageRes = R.drawable.ic_onboarding_2
            ),
            OnboardingPage(
                title = UiText.StringResource(R.string.onboarding_slide3_title),
                description = UiText.StringResource(R.string.onboarding_slide3_desc),
                imageRes = R.drawable.ic_onboarding_3
            )
        )
    }
}