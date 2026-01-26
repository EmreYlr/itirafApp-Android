package com.itirafapp.android.presentation.screens.home.dm_request

data class DMRequestState(
    val isLoading: Boolean = false,
    val shareSocialLinks: Boolean = true,
    val initialMessage: String = "",
    val error: String? = null,
)

sealed class DMRequestEvent {
    data class Init(val postId: Int) : DMRequestEvent()
    data class MessageChanged(val message: String) : DMRequestEvent()
    data class ShareLinksToggled(val isChecked: Boolean) : DMRequestEvent()
    object SubmitClicked : DMRequestEvent()
}

sealed class DMRequestUiEvent {
    object Dismiss : DMRequestUiEvent()
    data class ShowMessage(val message: String) : DMRequestUiEvent()
}