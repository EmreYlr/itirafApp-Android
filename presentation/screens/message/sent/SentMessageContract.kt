package com.itirafapp.android.presentation.screens.message.sent

import com.itirafapp.android.domain.model.SentMessage

data class SentMessageState(
    val inboxMessage: List<SentMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isRefreshing: Boolean = false
)

sealed class SentMessageEvent {
    object Refresh : SentMessageEvent()
    data class ItemClicked(val id: String) : SentMessageEvent()
}

sealed class SentMessageUiEvent {
    data class NavigateToDetail(val id: String) : SentMessageUiEvent()
    data class ShowMessage(val message: String) : SentMessageUiEvent()
}