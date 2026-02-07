package com.itirafapp.android.presentation.screens.message.direct_message

import com.itirafapp.android.domain.model.DirectMessage

data class DirectMessageState(
    val directMessages: List<DirectMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isRefreshing: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val selectedRoomId: String? = null
)

sealed class DirectMessageEvent {
    object Refresh : DirectMessageEvent()
    data class DirectMessageClicked(val id: String, val title: String) : DirectMessageEvent()
    data class OnLongClick(val id: String) : DirectMessageEvent()
    object DismissDeleteDialog : DirectMessageEvent()
    data class DeleteRoom(val blockUser: Boolean) : DirectMessageEvent()
}

sealed class DirectMessageUiEvent {
    data class NavigateToDetail(val id: String, val title: String) : DirectMessageUiEvent()
    data class ShowMessage(val message: String) : DirectMessageUiEvent()
}