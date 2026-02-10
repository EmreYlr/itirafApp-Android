package com.itirafapp.android.presentation.screens.message.inbox.inbox_detail

import com.itirafapp.android.domain.model.InboxMessage
import com.itirafapp.android.util.state.UiText

data class InboxDetailState(
    val isLoading: Boolean = false,
    val inboxMessage: InboxMessage? = null,
    val error: UiText? = null,
    val showDeleteDialog: Boolean = false,
)

sealed class InboxDetailEvent {
    object BackClicked : InboxDetailEvent()
    object ApproveClicked : InboxDetailEvent()
    object RejectClicked : InboxDetailEvent()
    data class OpenSocialLink(val url: String) : InboxDetailEvent()
    object BlockUserClicked : InboxDetailEvent()
}

sealed class InboxDetailUiEvent {
    object NavigateToBack : InboxDetailUiEvent()
    data class NavigateToUrl(val url: String) : InboxDetailUiEvent()
    data class NavigateToChat(val roomId: String, val roomName: String) : InboxDetailUiEvent()
    data class ShowMessage(val message: UiText) : InboxDetailUiEvent()
}