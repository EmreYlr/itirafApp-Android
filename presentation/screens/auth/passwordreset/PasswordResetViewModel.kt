package com.itirafapp.android.presentation.screens.auth.passwordreset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.data.remote.auth.dto.ResetPasswordRequest
import com.itirafapp.android.domain.usecase.auth.ResetPasswordUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val passwordResetUseCase: ResetPasswordUseCase
) : ViewModel() {
    var state by mutableStateOf(PasswordResetState())
        private set

    private val _uiEvent = Channel<PasswordResetUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: PasswordResetEvent) {
        when (event) {
            is PasswordResetEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is PasswordResetEvent.ResetClicked -> {
                resetPassword()
            }
            is PasswordResetEvent.BackClicked -> {
                sendUiEvent(PasswordResetUiEvent.NavigateToBack)
            }
        }
    }

    fun resetPassword() {
        if (state.email.isBlank()) {
            sendUiEvent(PasswordResetUiEvent.ShowMessage("Email cannot be empty"))
            return
        }

        passwordResetUseCase(
            ResetPasswordRequest (email = state.email)
        ).onEach { result ->

            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }
                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(PasswordResetUiEvent.ShowMessage("Email gönderildi. Mailinizi kontrol edin."))
                    sendUiEvent(PasswordResetUiEvent.NavigateToBack)
                }
                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(PasswordResetUiEvent.ShowMessage(result.message ?: "Email gönderilemedi"))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: PasswordResetUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}