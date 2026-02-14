package com.itirafapp.android.presentation.screens.message.sent

import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.util.state.UiText

data class SentMessageState(
    val sentMessage: List<SentMessage> = emptyList(),
    val isLoading: Boolean = true,
    val error: UiText? = null,
    val isRefreshing: Boolean = false
)

sealed class SentMessageEvent {
    object Refresh : SentMessageEvent()
    object LoadData : SentMessageEvent()
    data class ItemClicked(val id: String) : SentMessageEvent()
}

sealed class SentMessageUiEvent {
    data class NavigateToDetail(val data: SentMessage) : SentMessageUiEvent()
    data class ShowMessage(val message: UiText) : SentMessageUiEvent()
}