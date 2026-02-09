package com.itirafapp.android.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.itirafapp.android.util.state.UiText

data class TermsItem(
    val title: UiText,
    val description: UiText,
    val icon: ImageVector
)