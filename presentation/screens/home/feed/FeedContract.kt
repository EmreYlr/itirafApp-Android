package com.itirafapp.android.presentation.screens.home.feed

import com.itirafapp.android.presentation.model.ConfessionUiModel
import com.itirafapp.android.util.state.UiText

data class FeedState(
    val isLoading: Boolean = false,
    val confessions: List<ConfessionUiModel> = emptyList(),
    val error: UiText? = null,
    val isRefreshing: Boolean = false
)

sealed class FeedEvent {
    object Refresh : FeedEvent()
    object LoadMore : FeedEvent()
    data class PostClicked(val id: Int) : FeedEvent()
    data class LikeClicked(val id: Int) : FeedEvent()
    data class ChannelClicked(val id: Int, val title: String) : FeedEvent()
    data class CommentClicked(val id: Int) : FeedEvent()
    data class DMRequestClicked(val id: Int) : FeedEvent()
    data class ShareClicked(val id: Int) : FeedEvent()
}

sealed class FeedUiEvent {
    data class NavigateToDetail(val id: Int) : FeedUiEvent()
    data class OpenDMSheet(val targetId: Int) : FeedUiEvent()
    data class OpenShareSheet(val link: String) : FeedUiEvent()
    data class NavigateToChannel(val id: Int, val title: String) : FeedUiEvent()
    data class ShowMessage(val message: UiText) : FeedUiEvent()
}