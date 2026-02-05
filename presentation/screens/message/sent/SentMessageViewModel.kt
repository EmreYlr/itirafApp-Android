package com.itirafapp.android.presentation.screens.message.sent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.room.GetSentMessagesUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentMessageViewModel @Inject constructor(
    private val getSentMessagesUseCase: GetSentMessagesUseCase
) : ViewModel() {

    var state by mutableStateOf(SentMessageState())
        private set

    private val _uiEvent = Channel<SentMessageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SentMessageEvent) {
        when (event) {
            is SentMessageEvent.Refresh -> {
                loadSentMessage(isRefresh = true)
            }

            is SentMessageEvent.ItemClicked -> {
                val selectedItem = state.sentMessage.find { it.requestId == event.id }
                selectedItem?.let {
                    sendUiEvent(SentMessageUiEvent.NavigateToDetail(it))
                }
            }
        }
    }

    fun loadSentMessage(isRefresh: Boolean = false) {
        getSentMessagesUseCase()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = !isRefresh,
                            isRefreshing = isRefresh,
                            error = ""
                        )
                    }

                    is Resource.Success -> {
                        state = state.copy(
                            sentMessage = result.data ?: emptyList(),
                            isLoading = false,
                            isRefreshing = false,
                            error = ""
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.message ?: "Beklenmedik bir hata oluştu"
                        )
                        sendUiEvent(SentMessageUiEvent.ShowMessage(result.message ?: "Hata"))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: SentMessageUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}