package com.itirafapp.android.presentation.screens.home.feed

import com.itirafapp.android.presentation.model.ConfessionUiModel

data class FeedState(
    val isLoading: Boolean = false,
    val confessions: List<ConfessionUiModel> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)

sealed class FeedEvent {
    object Refresh : FeedEvent()
    object LoadMore : FeedEvent()
    data class PostClicked(val id: Int) : FeedEvent()
    data class LikeClicked(val id: Int) : FeedEvent()
    data class ChannelClicked(val id: Int) : FeedEvent()
    data class CommentClicked(val id: Int) : FeedEvent()
    data class DMRequestClicked(val id: Int) : FeedEvent()
    data class ShareClicked(val id: Int) : FeedEvent()
}

sealed class FeedUiEvent {
    data class NavigateToDetail(val id: Int) : FeedUiEvent()
    data class ShowMessage(val message: String) : FeedUiEvent()
}