package com.itirafapp.android.presentation.screens.auth.passwordreset

data class PasswordResetState(
    val email: String = "",
    val isLoading: Boolean = false
)

sealed class PasswordResetEvent {
    data class EmailChanged(val email: String) : PasswordResetEvent()
    object ResetClicked : PasswordResetEvent()
    object BackClicked : PasswordResetEvent()
}

sealed class PasswordResetUiEvent {
    object NavigateToBack : PasswordResetUiEvent()
    data class ShowMessage(val message: String) : PasswordResetUiEvent()
}