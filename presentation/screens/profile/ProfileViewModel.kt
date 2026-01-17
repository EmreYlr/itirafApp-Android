package com.itirafapp.android.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.SettingsClicked -> {
                sendUiEvent(ProfileUiEvent.NavigateToSettings)
            }
        }
    }

    private fun sendUiEvent(event: ProfileUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}


