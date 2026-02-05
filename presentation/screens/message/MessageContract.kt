package com.itirafapp.android.presentation.screens.message

data class MessageState(
    val selectedTabIndex: Int = 0,
)

sealed class MessageEvent {
    data class TabChanged(val index: Int) : MessageEvent()
    object SentMessageClicked : MessageEvent()
}

sealed class MessageUiEvent {
    object NavigateToSentMessage : MessageUiEvent()
}