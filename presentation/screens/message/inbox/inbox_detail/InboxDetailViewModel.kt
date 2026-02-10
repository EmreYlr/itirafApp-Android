package com.itirafapp.android.presentation.screens.message.inbox.inbox_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.InboxMessage
import com.itirafapp.android.domain.usecase.room.ApproveMessageRequestUseCase
import com.itirafapp.android.domain.usecase.room.RejectMessageRequestUseCase
import com.itirafapp.android.domain.usecase.user.BlockUserUseCase
import com.itirafapp.android.presentation.screens.home.detail.DetailUiEvent
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
class InboxDetailViewModel @Inject constructor(
    private val approveMessageRequestUseCase: ApproveMessageRequestUseCase,
    private val rejectMessageRequestUseCase: RejectMessageRequestUseCase,
    private val blockUserUseCase: BlockUserUseCase
) : ViewModel() {

    var state by mutableStateOf(InboxDetailState())
        private set

    private val _uiEvent = Channel<InboxDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setInitialData(data: InboxMessage) {
        if (state.inboxMessage == null) {
            state = state.copy(
                inboxMessage = data
            )
        }
    }

    fun onEvent(event: InboxDetailEvent) {
        when (event) {
            is InboxDetailEvent.BackClicked -> {
                sendUiEvent(InboxDetailUiEvent.NavigateToBack)
            }

            is InboxDetailEvent.ApproveClicked -> {
                approveMessage()
            }

            is InboxDetailEvent.RejectClicked -> {
                rejectMessage()
            }

            is InboxDetailEvent.OpenSocialLink -> {
                sendUiEvent(InboxDetailUiEvent.NavigateToUrl(event.url))
            }

            is InboxDetailEvent.BlockUserClicked -> {
                blockUser()
            }
        }
    }

    private fun approveMessage() {
        val inboxMessage = state.inboxMessage ?: return

        approveMessageRequestUseCase(inboxMessage.requestId)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        state = state.copy(isLoading = false)
                        sendUiEvent(
                            InboxDetailUiEvent.NavigateToChat(
                                inboxMessage.roomId,
                                inboxMessage.requesterUsername
                            )
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.error.message
                        )
                        sendUiEvent(InboxDetailUiEvent.ShowMessage(result.error.message))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun rejectMessage() {
        val requestId = state.inboxMessage?.requestId ?: return

        rejectMessageRequestUseCase(requestId)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        state = state.copy(isLoading = false)
                        sendUiEvent(InboxDetailUiEvent.NavigateToBack)
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.error.message
                        )
                        sendUiEvent(InboxDetailUiEvent.ShowMessage(result.error.message))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun blockUser() {
        val targetUserId = state.inboxMessage?.requesterUserId ?: return

        blockUserUseCase(targetUserId)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        state = state.copy(isLoading = false)
                        sendUiEvent(
                            InboxDetailUiEvent.ShowMessage(
                                UiText.StringResource(R.string.message_user_blocked)
                            )
                        )
                        sendUiEvent(InboxDetailUiEvent.NavigateToBack)
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.error.message
                        )
                        sendUiEvent(
                            InboxDetailUiEvent.ShowMessage(
                                result.error.message
                            )
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: InboxDetailUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}