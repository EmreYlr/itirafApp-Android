package com.itirafapp.android.presentation.screens.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.data.remote.auth.dto.RegisterRequest
import com.itirafapp.android.domain.usecase.auth.RegisterUserUseCase
import com.itirafapp.android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    private val _uiEvent = Channel<RegisterUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is RegisterEvent.PasswordChanged ->  {
                state = state.copy(password = event.password)
            }
            is RegisterEvent.PrivacyPolicyChanged -> {
                state = state.copy(isPrivacyAccepted = event.isAccepted)
            }
            is RegisterEvent.TermsChanged -> {
                state = state.copy(isTermsAccepted = event.isAccepted)
            }
            is RegisterEvent.OpenPrivacyPolicy -> {
                sendUiEvent(RegisterUiEvent.ShowPrivacyPolicyDialog)
            }
            is RegisterEvent.OpenTermsOfUse -> {
                sendUiEvent(RegisterUiEvent.ShowTermsDialog)
            }
            is RegisterEvent.RegisterClicked -> {
                register()
            }
            is RegisterEvent.LoginClicked -> {
                sendUiEvent(RegisterUiEvent.NavigateToLogin)
            }
        }
    }

    private fun register() {
        if (state.email.isBlank() || state.password.isBlank()) {
            sendUiEvent(RegisterUiEvent.ShowMessage("Lütfen tüm alanları doldurun"))
            return
        }

        if (!state.isPrivacyAccepted || !state.isTermsAccepted) {
            sendUiEvent(RegisterUiEvent.ShowMessage("Lütfen sözleşmeleri okuyup onaylayın."))
            return
        }

        registerUserUseCase(
            RegisterRequest(email = state.email, password = state.password)
        ).onEach { result ->

            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }
                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(RegisterUiEvent.ShowMessage("Kayıt Başarılı! Giriş yapabilirsiniz."))
                    sendUiEvent(RegisterUiEvent.NavigateToLogin)
                }
                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(RegisterUiEvent.ShowMessage(result.message ?: "Kayıt başarısız"))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: RegisterUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

}