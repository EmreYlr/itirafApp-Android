package com.itirafapp.android.presentation.screens.auth.login

import com.itirafapp.android.util.state.UiText

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object LoginClicked : LoginEvent()
    object AnonymousLoginClicked : LoginEvent()
    object RegisterClicked : LoginEvent()
    object ForgotPasswordClicked : LoginEvent()
    object OpenPrivacyPolicy : LoginEvent()
    object OpenTermsOfUse : LoginEvent()
    data class OnGoogleLoginSuccess(val token: String) : LoginEvent()
    object OnGoogleLoginError : LoginEvent()
    object ResendEmailClicked : LoginEvent()
    object DismissResendDialog : LoginEvent()
}

sealed class LoginUiEvent {
    object NavigateToHome : LoginUiEvent()
    object NavigateToRegister : LoginUiEvent()
    object NavigateToForgotPassword : LoginUiEvent()
    object ShowPrivacyPolicyDialog : LoginUiEvent()
    object ShowTermsDialog : LoginUiEvent()
    object ShowResendDialog : LoginUiEvent()
    object HideResendDialog : LoginUiEvent()
    data class ShowMessage(val message: UiText) : LoginUiEvent()
}