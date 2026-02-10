package com.itirafapp.android.presentation.screens.message.sent.sent_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.domain.usecase.room.DeleteSentMessageRequestUseCase
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
class SentMessageDetailViewModel @Inject constructor(
    private val deleteSentMessageRequestUseCase: DeleteSentMessageRequestUseCase
) : ViewModel() {

    var state by mutableStateOf(SentMessageDetailState())
        private set

    private val _uiEvent = Channel<SentMessageDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setInitialData(data: SentMessage) {
        if (state.sentMessage == null) {
            state = state.copy(
                sentMessage = data
            )
        }
    }

    fun onEvent(event: SentMessageDetailEvent) {
        when (event) {
            is SentMessageDetailEvent.BackClicked -> {
                sendUiEvent(SentMessageDetailUiEvent.NavigateToBack)
            }

            is SentMessageDetailEvent.DeleteConfirmed -> {
                deleteRequest()
            }

            is SentMessageDetailEvent.DeleteDialogDismissed -> {
                state = state.copy(showDeleteDialog = false)
            }

            is SentMessageDetailEvent.DeleteIconClicked -> {
                state = state.copy(showDeleteDialog = true)
            }
        }
    }

    private fun deleteRequest() {
        val requestId = state.sentMessage?.requestId ?: return

        deleteSentMessageRequestUseCase(requestId)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = true,
                            showDeleteDialog = false,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        state = state.copy(isLoading = false)
                        sendUiEvent(
                            SentMessageDetailUiEvent.ShowMessage(
                                UiText.StringResource(R.string.message_request_withdrawn)
                            )
                        )
                        sendUiEvent(SentMessageDetailUiEvent.NavigateToBack)
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.error.message
                        )
                        sendUiEvent(
                            SentMessageDetailUiEvent.ShowMessage(
                                result.error.message
                            )
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: SentMessageDetailUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}