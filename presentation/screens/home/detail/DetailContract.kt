package com.itirafapp.android.presentation.screens.home.detail

import com.itirafapp.android.presentation.model.ConfessionDetailUiModel

data class DetailState(
    val isLoading: Boolean = false,
    val confession: ConfessionDetailUiModel? = null,
    val commentText: String = "",
    val isSendingComment: Boolean = false,
    val error: String? = null,
)

sealed class DetailEvent {
    object BackClicked : DetailEvent()
    data class LikeClicked(val id: Int) : DetailEvent()
    data class DMRequestClicked(val id: Int) : DetailEvent()
    data class ShareClicked(val id: Int) : DetailEvent()
    data class MoreClicked(val id: Int) : DetailEvent()
    data class CommentTextChanged(val text: String) : DetailEvent()
    object SendCommentClicked : DetailEvent()
}

sealed class DetailUiEvent {
    object NavigateToBack : DetailUiEvent()
    data class OpenShareSheet(val link: String) : DetailUiEvent()
    data class ShowMessage(val message: String) : DetailUiEvent()
}