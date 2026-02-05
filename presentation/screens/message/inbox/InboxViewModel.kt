package com.itirafapp.android.presentation.screens.message.inbox

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.room.ApproveMessageRequestUseCase
import com.itirafapp.android.domain.usecase.room.GetPendingMessagesUseCase
import com.itirafapp.android.domain.usecase.room.RejectMessageRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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

            }

            is InboxEvent.RejectClicked -> {

            }
        }
    }

    private fun loadInbox(isRefresh: Boolean = false) {

    }

    private fun sendUiEvent(event: InboxUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}