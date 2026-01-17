package com.itirafapp.android.presentation.screens.auth.login

data class LoginState(
    val email: String = "admin@admin.com",
    val password: String = "password123",
    val isLoading: Boolean = false
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object LoginClicked : LoginEvent()
    object RegisterClicked : LoginEvent()
    object ForgotPasswordClicked : LoginEvent()
    object OpenPrivacyPolicy : LoginEvent()
    object OpenTermsOfUse : LoginEvent()
}

sealed class LoginUiEvent {
    object NavigateToHome : LoginUiEvent()
    object NavigateToRegister : LoginUiEvent()
    object NavigateToForgotPassword : LoginUiEvent()
    object ShowPrivacyPolicyDialog : LoginUiEvent()
    object ShowTermsDialog : LoginUiEvent()
    data class ShowError(val message: String) : LoginUiEvent()
}