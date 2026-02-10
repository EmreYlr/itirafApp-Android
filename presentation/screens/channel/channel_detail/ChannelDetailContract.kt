package com.itirafapp.android.presentation.screens.channel.channel_detail

import com.itirafapp.android.presentation.model.ConfessionUiModel
import com.itirafapp.android.util.state.UiText

data class ChannelDetailState(
    val channelId: Int = 0,
    val channelTitle: String = "",

    val isFollowing: Boolean = false,
    val confessions: List<ConfessionUiModel> = emptyList(),
    val isUserAuthenticated: Boolean = false,

    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: UiText? = null
)

sealed class ChannelDetailEvent {
    object ToggleFollow : ChannelDetailEvent()
    object LoadMore : ChannelDetailEvent()
    object Refresh : ChannelDetailEvent()
    object BackClicked : ChannelDetailEvent()
    data class ConfessionClicked(val id: Int) : ChannelDetailEvent()
    data class LikeConfession(val id: Int) : ChannelDetailEvent()
    data class AddPostClicked(val id: Int) : ChannelDetailEvent()
}

sealed class ChannelDetailUiEvent {
    object NavigateBack : ChannelDetailUiEvent()
    data class NavigateToConfessionDetail(val id: Int) : ChannelDetailUiEvent()
    data class NavigateToAddPost(val id: Int) : ChannelDetailUiEvent()
    data class ShowMessage(val message: UiText) : ChannelDetailUiEvent()
}