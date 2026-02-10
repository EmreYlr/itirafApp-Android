package com.itirafapp.android.presentation.screens.message.direct_message.chat

import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.presentation.model.ChatUiItem
import com.itirafapp.android.util.state.UiText

data class ChatState(
    val roomId: String = "",
    val roomName: String = "",
    val messages: List<ChatUiItem> = emptyList(),
    val messageInput: String = "",
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val page: Int = 1,
    val hasNextPage: Boolean = false,
    val showBlockDialog: Boolean = false,
    val error: UiText? = null,
)

sealed class ChatEvent {
    data class MessageInputChanged(val input: String) : ChatEvent()
    object SendMessage : ChatEvent()
    object LoadMoreMessage : ChatEvent()
    object BlockUserClicked : ChatEvent()
    object DismissBlockDialog : ChatEvent()
    object BlockUserConfirmed : ChatEvent()
    object ReportUserClicked : ChatEvent()

}

sealed class ChatUiEvent {
    object NavigateToBack : ChatUiEvent()
    data class OpenReportSheet(val target: ReportTarget) : ChatUiEvent()
    object ScrollToBottom : ChatUiEvent()
    data class ShowMessage(val message: UiText) : ChatUiEvent()
}