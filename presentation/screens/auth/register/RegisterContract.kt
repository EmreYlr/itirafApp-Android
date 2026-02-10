package com.itirafapp.android.presentation.screens.auth.register

import com.itirafapp.android.util.state.UiText

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val isPrivacyAccepted: Boolean = false,
    val isTermsAccepted: Boolean = false,
    val isLoading: Boolean = false
)

sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    object LoginClicked : RegisterEvent()
    object RegisterClicked : RegisterEvent()
    data class PrivacyPolicyChanged(val isAccepted: Boolean) : RegisterEvent()
    data class TermsChanged(val isAccepted: Boolean) : RegisterEvent()
    object OpenPrivacyPolicy : RegisterEvent()
    object OpenTermsOfUse : RegisterEvent()
    object ResendEmailClicked : RegisterEvent()

    object DismissResendDialog : RegisterEvent()
}

sealed class RegisterUiEvent {
    object NavigateToLogin : RegisterUiEvent()
    object ShowSuccessDialog : RegisterUiEvent()
    object ShowPrivacyPolicyDialog : RegisterUiEvent()
    object ShowResendDialog : RegisterUiEvent()
    object HideResendDialog : RegisterUiEvent()
    object ShowTermsDialog : RegisterUiEvent()
    data class ShowMessage(val message: UiText) : RegisterUiEvent()
}