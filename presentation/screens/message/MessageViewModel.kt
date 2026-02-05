package com.itirafapp.android.presentation.screens.message

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
class MessageViewModel @Inject constructor(
) : ViewModel() {
    var state by mutableStateOf(MessageState())
        private set

    private val _uiEvent = Channel<MessageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.TabChanged -> {
                state = state.copy(selectedTabIndex = event.index)
            }

            is MessageEvent.SentMessageClicked -> {
                sendUiEvent(MessageUiEvent.NavigateToSentMessage)
            }
        }
    }

    private fun sendUiEvent(event: MessageUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}