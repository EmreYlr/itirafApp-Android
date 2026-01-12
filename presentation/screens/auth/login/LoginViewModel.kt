package com.itirafapp.android.presentation.screens.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.data.remote.dto.LoginRequest
import com.itirafapp.android.domain.usecase.LoginUserUseCase
import com.itirafapp.android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
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
            is LoginEvent.LoginClicked -> {
                login()
            }
            is LoginEvent.RegisterClicked -> {
                sendUiEvent(LoginUiEvent.NavigateToRegister)
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = loginUserUseCase(
                LoginRequest(email = state.email, password = state.password)
            )

            state = state.copy(isLoading = false)

            when (result) {
                is Resource.Success -> {
                    sendUiEvent(LoginUiEvent.NavigateToHome)
                }
                is Resource.Error -> {
                    sendUiEvent(LoginUiEvent.ShowError(result.message ?: "Giriş başarısız"))
                }
                else -> Unit
            }
        }
    }

    private fun sendUiEvent(event: LoginUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}