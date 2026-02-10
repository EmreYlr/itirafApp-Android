package com.itirafapp.android.presentation.screens.message.direct_message

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.domain.usecase.room.DeleteRoomUseCase
import com.itirafapp.android.domain.usecase.room.GetAllDirectMessagesUseCase
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
class DirectMessageViewModel @Inject constructor(
    private val getAllDirectMessagesUseCase: GetAllDirectMessagesUseCase,
    private val deleteRoomUseCase: DeleteRoomUseCase
) : ViewModel() {

    var state by mutableStateOf(DirectMessageState())
        private set

    private val _uiEvent = Channel<DirectMessageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadMessages()
    }

    fun onEvent(event: DirectMessageEvent) {
        when (event) {
            is DirectMessageEvent.Refresh -> {
                loadMessages(isRefresh = true)
            }

            is DirectMessageEvent.DirectMessageClicked -> {
                sendUiEvent(DirectMessageUiEvent.NavigateToDetail(event.id, event.title))
            }

            is DirectMessageEvent.OnLongClick -> {
                state = state.copy(
                    selectedRoomId = event.id,
                    showDeleteDialog = true
                )
            }

            is DirectMessageEvent.DismissDeleteDialog -> {
                state = state.copy(
                    showDeleteDialog = false,
                    selectedRoomId = null
                )
            }

            is DirectMessageEvent.DeleteRoom -> {
                val roomId = state.selectedRoomId
                if (roomId != null) {
                    deleteRoom(roomId, event.blockUser)
                }
            }
        }
    }

    private fun loadMessages(isRefresh: Boolean = false) {
        getAllDirectMessagesUseCase()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = !isRefresh,
                            isRefreshing = isRefresh,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        state = state.copy(
                            directMessages = result.data ?: emptyList(),
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.error.message
                        )
                        sendUiEvent(DirectMessageUiEvent.ShowMessage(result.error.message))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun deleteRoom(roomId: String, blockUser: Boolean) {
        deleteRoomUseCase(roomId, blockUser)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(showDeleteDialog = false)
                    }

                    is Resource.Success -> {
                        val updatedList = state.directMessages.filter { it.roomId != roomId }

                        state = state.copy(
                            directMessages = updatedList,
                            selectedRoomId = null,
                            showDeleteDialog = false
                        )
                        sendUiEvent(
                            DirectMessageUiEvent.ShowMessage(
                                UiText.StringResource(R.string.message_chat_deleted)
                            )
                        )

                        loadMessages(isRefresh = true)
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            showDeleteDialog = false,
                            selectedRoomId = null
                        )
                        sendUiEvent(
                            DirectMessageUiEvent.ShowMessage(
                                result.error.message
                            )
                        )
                        loadMessages()
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: DirectMessageUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}