package com.itirafapp.android.presentation.screens.message.inbox

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.room.ApproveMessageRequestUseCase
import com.itirafapp.android.domain.usecase.room.GetPendingMessagesUseCase
import com.itirafapp.android.domain.usecase.room.RejectMessageRequestUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val getPendingMessagesUseCase: GetPendingMessagesUseCase,
    private val approveMessageRequestUseCase: ApproveMessageRequestUseCase,
    private val rejectMessageRequestUseCase: RejectMessageRequestUseCase
) : ViewModel() {

    var state by mutableStateOf(InboxState())
        private set

    private val _uiEvent = Channel<InboxUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadInbox()
    }

    fun onEvent(event: InboxEvent) {
        when (event) {
            is InboxEvent.Refresh -> {
                loadInbox(isRefresh = true)
            }

            is InboxEvent.InboxClicked -> {
                sendUiEvent(InboxUiEvent.NavigateToDetail(event.id))
            }

            is InboxEvent.ApproveClicked -> {
                approveRequest(event.id)
            }

            is InboxEvent.RejectClicked -> {
                rejectRequest(event.id)
            }
        }
    }

    private fun loadInbox(isRefresh: Boolean = false) {
        getPendingMessagesUseCase()
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
                            inboxMessage = result.data ?: emptyList(),
                            isLoading = false,
                            isRefreshing = false,
                            error = ""
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.message ?: "Hata"
                        )
                        sendUiEvent(InboxUiEvent.ShowMessage(result.message ?: "Yüklenemedi"))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun approveRequest(requestId: String) {
        approveMessageRequestUseCase(requestId)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        removeItemFromState(requestId)
                    }

                    is Resource.Error -> {
                        sendUiEvent(InboxUiEvent.ShowMessage(result.message ?: "İşlem başarısız"))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun rejectRequest(requestId: String) {
        rejectMessageRequestUseCase(requestId)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {}

                    is Resource.Success -> {
                        removeItemFromState(requestId)
                    }

                    is Resource.Error -> {
                        sendUiEvent(InboxUiEvent.ShowMessage(result.message ?: "İşlem başarısız"))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun removeItemFromState(requestId: String) {
        val currentList = state.inboxMessage.toMutableList()
        currentList.removeAll { it.requestId == requestId }
        state = state.copy(inboxMessage = currentList)
    }

    private fun sendUiEvent(event: InboxUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}