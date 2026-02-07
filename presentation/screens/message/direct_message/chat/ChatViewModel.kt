package com.itirafapp.android.presentation.screens.message.direct_message.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.domain.usecase.chat.ConnectChatUseCase
import com.itirafapp.android.domain.usecase.chat.DisconnectChatUseCase
import com.itirafapp.android.domain.usecase.chat.ObserveChatMessagesUseCase
import com.itirafapp.android.domain.usecase.chat.SendMessageUseCase
import com.itirafapp.android.domain.usecase.room.DeleteRoomUseCase
import com.itirafapp.android.domain.usecase.room.GetRoomMessagesUseCase
import com.itirafapp.android.presentation.mapper.toUiModel
import com.itirafapp.android.presentation.model.ChatUiItem
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getRoomMessagesUseCase: GetRoomMessagesUseCase,
    private val deleteRoomUseCase: DeleteRoomUseCase,
    connectChatUseCase: ConnectChatUseCase,
    private val disconnectChatUseCase: DisconnectChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeChatMessagesUseCase: ObserveChatMessagesUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val roomId: String = checkNotNull(savedStateHandle.get<String>("roomId"))
    private val roomTitle: String = checkNotNull(savedStateHandle.get<String>("roomTitle"))

    private var rawMessages: List<MessageData> = emptyList()

    var state by mutableStateOf(ChatState())
        private set

    private val _uiEvent = Channel<ChatUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        state = state.copy(
            roomId = roomId,
            roomName = roomTitle
        )

        loadMessages()

        connectChatUseCase(roomId)

        observeLiveMessages()
    }

    override fun onCleared() {
        super.onCleared()
        disconnectChatUseCase()
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.MessageInputChanged -> {
                state = state.copy(messageInput = event.input)
            }

            is ChatEvent.SendMessage -> {
                sendMessage()
            }

            is ChatEvent.BlockUserClicked -> {
                state = state.copy(showBlockDialog = true)
            }

            is ChatEvent.DismissBlockDialog -> {
                state = state.copy(showBlockDialog = false)
            }

            is ChatEvent.BlockUserConfirmed -> {
                blockRoom(state.roomId)
            }

            is ChatEvent.ReportUserClicked -> {
                sendUiEvent(ChatUiEvent.OpenReportSheet(ReportTarget.Room(state.roomId)))
            }

            is ChatEvent.LoadMoreMessage -> {
                if (state.hasNextPage && !state.isLoadingMore && !state.isLoading) {
                    loadMessages(isLoadMore = true)
                }
            }
        }
    }

    private fun observeLiveMessages() {
        observeChatMessagesUseCase()
            .onEach { newMessage ->
                val updatedList = listOf(newMessage) + rawMessages
                rawMessages = updatedList

                val uiMessages = mapToUiModels(rawMessages)

                state = state.copy(messages = uiMessages)

                sendUiEvent(ChatUiEvent.ScrollToBottom)
            }
            .launchIn(viewModelScope)
    }

    private fun loadMessages(isLoadMore: Boolean = false) {
        if (state.isLoading || state.isLoadingMore) return

        val currentPage = if (isLoadMore) state.page + 1 else 1

        getRoomMessagesUseCase(
            roomId = state.roomId,
            page = currentPage
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = if (isLoadMore) {
                        state.copy(isLoadingMore = true, error = "")
                    } else {
                        state.copy(isLoading = true, error = "")
                    }
                }

                is Resource.Success -> {
                    result.data?.let { data ->
                        val newMessages = data.items

                        rawMessages = if (isLoadMore) {
                            rawMessages + newMessages
                        } else {
                            newMessages
                        }

                        val uiMessages = mapToUiModels(rawMessages)

                        state = state.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            page = data.page,
                            hasNextPage = data.hasNextPage,
                            messages = uiMessages
                        )

                        if (!isLoadMore) {
                            sendUiEvent(ChatUiEvent.ScrollToBottom)
                        }
                    }
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = result.message ?: "Mesajlar yüklenemedi"
                    )
                    sendUiEvent(ChatUiEvent.ShowMessage(result.message ?: "Hata oluştu"))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun mapToUiModels(messages: List<MessageData>): List<ChatUiItem> {
        return messages.mapIndexed { index, message ->
            val messageBelow = messages.getOrNull(index - 1)
            val showTime = if (messageBelow != null) {
                message.isMyMessage != messageBelow.isMyMessage
            } else {
                true
            }

            val showProfileImage = !message.isMyMessage &&
                    (messageBelow == null || messageBelow.isMyMessage)

            message.toUiModel(showTime = showTime, showProfileImage = showProfileImage)
        }
    }

    private fun sendMessage() {
        val textToSend = state.messageInput.trim()
        if (textToSend.isBlank()) return

        val tempMessage = MessageData(
            id = System.currentTimeMillis().toInt(),
            content = textToSend,
            createdAt = Instant.now().toString(),
            isMyMessage = true,
            isSeen = false,
            seenAt = null
        )

        val updatedList = listOf(tempMessage) + rawMessages
        rawMessages = updatedList

        state = state.copy(
            messages = mapToUiModels(rawMessages),
            messageInput = ""
        )

        sendUiEvent(ChatUiEvent.ScrollToBottom)

        sendMessageUseCase(textToSend)
    }

    private fun blockRoom(roomId: String) {
        deleteRoomUseCase(roomId, true)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        state = state.copy(
                            isLoading = false,
                            showBlockDialog = false
                        )

                        sendUiEvent(ChatUiEvent.ShowMessage("Kullanıcı Engellendi."))
                        sendUiEvent(ChatUiEvent.NavigateToBack)
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            showBlockDialog = false,
                        )
                        sendUiEvent(
                            ChatUiEvent.ShowMessage(
                                result.message ?: "Kullanıcı Engellenemedi"
                            )
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }


    private fun sendUiEvent(event: ChatUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}