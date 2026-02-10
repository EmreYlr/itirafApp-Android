package com.itirafapp.android.presentation.screens.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.data.remote.auth.dto.RegisterRequest
import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.domain.usecase.auth.RegisterUserUseCase
import com.itirafapp.android.util.extension.refinedForLogin
import com.itirafapp.android.util.extension.refinedForRegister
import com.itirafapp.android.util.state.Resource
import com.itirafapp.android.util.state.UiText
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
            val error =
                AppError.ValidationError.EmptyField(UiText.StringResource(R.string.email_or_password))
            sendUiEvent(RegisterUiEvent.ShowMessage(error.message))
            return
        }

        if (!state.isPrivacyAccepted || !state.isTermsAccepted) {
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
                    //sendUiEvent(RegisterUiEvent.ShowMessage("Kayıt Başarılı! Giriş yapabilirsiniz."))
                    //TODO: EMAİL ONAYI İLETİLECEK.
                    sendUiEvent(RegisterUiEvent.NavigateToLogin)
                }
                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    val originalError = result.error

                    val refinedError = originalError.refinedForRegister()

                    sendUiEvent(RegisterUiEvent.ShowMessage(refinedError.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: RegisterUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

}