package com.itirafapp.android.presentation.model

import androidx.annotation.DrawableRes
import com.itirafapp.android.util.state.UiText

data class OnboardingPage(
    val title: UiText,
    val description: UiText,
    @param:DrawableRes val imageRes: Int
)