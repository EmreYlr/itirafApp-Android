package com.itirafapp.android.presentation.screens.terms

import com.itirafapp.android.presentation.model.TermsItem
import com.itirafapp.android.util.state.UiText

data class TermsState(
    val items: List<TermsItem> = emptyList(),
    val isAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed class TermsEvent {
    data class OnToggleAcceptance(val isChecked: Boolean) : TermsEvent()
    object OnPrivacyPolicyClick : TermsEvent()
    object OnStartClick : TermsEvent()
}

sealed class TermsUiEvent {
    object NavigateToHome : TermsUiEvent()
    object NavigateToAuth : TermsUiEvent()
    data class NavigateToUrl(val url: String) : TermsUiEvent()
}