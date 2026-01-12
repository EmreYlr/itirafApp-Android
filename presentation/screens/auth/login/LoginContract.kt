package com.itirafapp.android.presentation.screens.auth.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object LoginClicked : LoginEvent()
    object RegisterClicked : LoginEvent()
}

sealed class LoginUiEvent {
    object NavigateToHome : LoginUiEvent()
    object NavigateToRegister : LoginUiEvent()
    data class ShowError(val message: String) : LoginUiEvent()
}