package com.itirafapp.android.presentation.screens.profile.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.auth.LogoutUserUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    private val _uiEvent = Channel<SettingsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.LogoutClicked -> {
                logout()
            }
        }
    }

    private fun logout() {
        logoutUserUseCase().onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }
                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(SettingsUiEvent.NavigateToLogin)
                }
                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(SettingsUiEvent.NavigateToLogin)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: SettingsUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}

