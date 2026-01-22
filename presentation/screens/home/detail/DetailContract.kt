package com.itirafapp.android.presentation.screens.home.detail

import com.itirafapp.android.presentation.model.ConfessionDetailUiModel

data class DetailState(
    val isLoading: Boolean = false,
    val confession: ConfessionDetailUiModel? = null,
    val error: String? = null,
)

sealed class DetailEvent {
    object BackClicked : DetailEvent()
    data class LikeClicked(val id: Int) : DetailEvent()
    data class CommentClicked(val id: Int) : DetailEvent()
    data class DMRequestClicked(val id: Int) : DetailEvent()
    data class ShareClicked(val id: Int) : DetailEvent()

}

sealed class DetailUiEvent {
    object NavigateToBack : DetailUiEvent()
    data class ShowMessage(val message: String) : DetailUiEvent()
}