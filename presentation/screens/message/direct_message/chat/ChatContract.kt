package com.itirafapp.android.presentation.screens.message.direct_message.chat

import com.itirafapp.android.presentation.model.ChatUiItem

data class ChatState(
    val roomId: String = "",
    val roomName: String = "",
    val messages: List<ChatUiItem> = emptyList(),
    val messageInput: String = "",
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val page: Int = 1,
    val hasNextPage: Boolean = false,
    val error: String = "",
)

sealed class ChatEvent {
    data class SetInitialData(val roomId: String, val roomName: String) : ChatEvent()
    data class MessageInputChanged(val input: String) : ChatEvent()
    object SendMessage : ChatEvent()
    object LoadMoreMessage : ChatEvent()
}

sealed class ChatUiEvent {
    object NavigateToBack : ChatUiEvent()
    object ScrollToBottom : ChatUiEvent()
    data class ShowMessage(val message: String) : ChatUiEvent()
}