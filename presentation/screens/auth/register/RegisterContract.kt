package com.itirafapp.android.presentation.screens.auth.register

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)

sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    object LoginClicked : RegisterEvent()
    object RegisterClicked : RegisterEvent()
}

sealed class RegisterUiEvent {
    object NavigateToLogin : RegisterUiEvent()
    data class ShowMessage(val message: String) : RegisterUiEvent()}