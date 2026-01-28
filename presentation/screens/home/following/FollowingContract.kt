package com.itirafapp.android.presentation.screens.home.following

import com.itirafapp.android.presentation.model.ConfessionUiModel

data class FollowingState(
    val isLoading: Boolean = false,
    val confessions: List<ConfessionUiModel> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)

sealed class FollowingEvent {
    object Refresh : FollowingEvent()
    object LoadMore : FollowingEvent()
    data class PostClicked(val id: Int) : FollowingEvent()
    data class LikeClicked(val id: Int) : FollowingEvent()
    data class ChannelClicked(val id: Int, val title: String) : FollowingEvent()
    data class CommentClicked(val id: Int) : FollowingEvent()
    data class DMRequestClicked(val id: Int) : FollowingEvent()
    data class ShareClicked(val id: Int) : FollowingEvent()
}

sealed class FollowingUiEvent {
    data class NavigateToDetail(val id: Int) : FollowingUiEvent()
    data class OpenDMSheet(val targetId: Int) : FollowingUiEvent()
    data class OpenShareSheet(val link: String) : FollowingUiEvent()
    data class NavigateToChannel(val id: Int, val title: String) : FollowingUiEvent()
    data class ShowMessage(val message: String) : FollowingUiEvent()
}