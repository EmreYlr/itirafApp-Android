package com.itirafapp.android.presentation.screens.message.inbox

import com.itirafapp.android.domain.model.InboxMessage

data class InboxState(
    val inboxMessage: List<InboxMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isRefreshing: Boolean = false
)

sealed class InboxEvent {
    object Refresh : InboxEvent()
    data class InboxClicked(val id: String) : InboxEvent()
    data class RejectClicked(val id: String) : InboxEvent()
    data class ApproveClicked(val id: String) : InboxEvent()
}

sealed class InboxUiEvent {
    data class NavigateToDetail(val data: InboxMessage) : InboxUiEvent()
    data class ShowMessage(val message: String) : InboxUiEvent()
}