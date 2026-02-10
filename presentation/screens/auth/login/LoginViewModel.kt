package com.itirafapp.android.presentation.screens.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.data.remote.auth.dto.GoogleLoginRequest
import com.itirafapp.android.data.remote.auth.dto.LoginRequest
import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.domain.usecase.auth.LoginAnonymousUseCase
import com.itirafapp.android.domain.usecase.auth.LoginGoogleUseCase
import com.itirafapp.android.domain.usecase.auth.LoginUserUseCase
import com.itirafapp.android.util.extension.refinedForLogin
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
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val loginAnonymousUseCase: LoginAnonymousUseCase,
    private val loginGoogleUseCase: LoginGoogleUseCase
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is LoginEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }

            is LoginEvent.OpenPrivacyPolicy -> {
                sendUiEvent(LoginUiEvent.ShowPrivacyPolicyDialog)
            }

            is LoginEvent.OpenTermsOfUse -> {
                sendUiEvent(LoginUiEvent.ShowTermsDialog)
            }

            is LoginEvent.LoginClicked -> {
                login()
            }

            is LoginEvent.AnonymousLoginClicked -> {
                anonymousLogin()
            }

            is LoginEvent.RegisterClicked -> {
                sendUiEvent(LoginUiEvent.NavigateToRegister)
            }

            is LoginEvent.ForgotPasswordClicked -> {
                sendUiEvent(LoginUiEvent.NavigateToForgotPassword)
            }

            is LoginEvent.OnGoogleLoginSuccess -> {
                loginWithGoogle(event.token)
            }

            is LoginEvent.OnGoogleLoginError -> {
                sendUiEvent(LoginUiEvent.ShowMessage(AppError.LocalError.Unknown.message))
            }
        }
    }

    private fun login() {
        if (state.email.isBlank() || state.password.isBlank()) {
            val error =
                AppError.ValidationError.EmptyField(UiText.StringResource(R.string.email_or_password))
            sendUiEvent(LoginUiEvent.ShowMessage(error.message))
            return
        }

        loginUserUseCase(
            LoginRequest(email = state.email, password = state.password)
        ).onEach { result ->

            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(LoginUiEvent.NavigateToHome)
                }

                is Resource.Error -> {
                    //TODO: 1405 gelirse resend email olacak aynısını register içinde yapcaz
                    val originalError = result.error

                    val refinedError = originalError.refinedForLogin()

                    state = state.copy(isLoading = false)
                    sendUiEvent(LoginUiEvent.ShowMessage(refinedError.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loginWithGoogle(idToken: String) {
        val request = GoogleLoginRequest(idToken = idToken)

        loginGoogleUseCase(request).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(LoginUiEvent.NavigateToHome)
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(
                        LoginUiEvent.ShowMessage(
                            result.error.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun anonymousLogin() {
        loginAnonymousUseCase().onEach { result ->

            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(LoginUiEvent.NavigateToHome)
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(LoginUiEvent.ShowMessage((result.error.message)))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: LoginUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}