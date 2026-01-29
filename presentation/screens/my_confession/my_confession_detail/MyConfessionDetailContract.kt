package com.itirafapp.android.presentation.screens.my_confession.my_confession_detail

import com.itirafapp.android.domain.model.MyConfessionData

data class MyConfessionDetailState(
    val isLoading: Boolean = false,
    val confessions: MyConfessionData? = null,
    val commentText: String = "",
    val currentUserId: String? = null,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed class MyConfessionDetailEvent {
    object BackClicked : MyConfessionDetailEvent()
    data class LikeClicked(val id: Int) : MyConfessionDetailEvent()
    data class ShareClicked(val id: Int) : MyConfessionDetailEvent()
    data class CommentTextChanged(val text: String) : MyConfessionDetailEvent()
    object SendCommentClicked : MyConfessionDetailEvent()
}

sealed class MyConfessionDetailUiEvent {
    object NavigateToBack : MyConfessionDetailUiEvent()
    data class OpenShareSheet(val link: String) : MyConfessionDetailUiEvent()
    data class ShowMessage(val message: String) : MyConfessionDetailUiEvent()
}