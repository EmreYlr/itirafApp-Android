package com.itirafapp.android.presentation.screens.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.data.remote.dto.LoginRequest
import com.itirafapp.android.data.remote.dto.RegisterRequest
import com.itirafapp.android.domain.usecase.LoginUserUseCase
import com.itirafapp.android.domain.usecase.RegisterUserUseCase
import com.itirafapp.android.presentation.navigation.Screen
import com.itirafapp.android.presentation.screens.auth.login.LoginEvent
import com.itirafapp.android.presentation.screens.auth.login.LoginUiEvent
import com.itirafapp.android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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
            is RegisterEvent.RegisterClicked -> {
                register()
            }
            is RegisterEvent.LoginClicked -> {
                sendUiEvent(RegisterUiEvent.NavigateToLogin)
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = registerUserUseCase(
                RegisterRequest(email = state.email, password = state.password)
            )

            state = state.copy(isLoading = false)

            when (result) {
                is Resource.Success -> {
                    sendUiEvent(RegisterUiEvent.ShowMessage("Kayıt Başarılı"))
                    sendUiEvent(RegisterUiEvent.NavigateToLogin)
                }
                is Resource.Error -> {
                    sendUiEvent(RegisterUiEvent.ShowMessage(result.message ?: "Kayıt başarısız"))
                }
                else -> Unit
            }
        }
    }

    private fun sendUiEvent(event: RegisterUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

}