package com.itirafapp.android.presentation.screens.message.sent.sent_detail

import com.itirafapp.android.domain.model.SentMessage

data class SentMessageDetailState(
    val isLoading: Boolean = false,
    val sentMessage: SentMessage? = null,
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
)

sealed class SentMessageDetailEvent {
    object BackClicked : SentMessageDetailEvent()
    object DeleteConfirmed : SentMessageDetailEvent()
    object DeleteIconClicked : SentMessageDetailEvent()
    object DeleteDialogDismissed : SentMessageDetailEvent()
}

sealed class SentMessageDetailUiEvent {
    object NavigateToBack : SentMessageDetailUiEvent()
    data class ShowMessage(val message: String) : SentMessageDetailUiEvent()
}