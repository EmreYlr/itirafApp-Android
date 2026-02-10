package com.itirafapp.android.presentation.screens.profile.follow_channel

import com.itirafapp.android.presentation.model.ChannelUiModel
import com.itirafapp.android.util.state.UiText

data class FollowChannelState(
    val isLoading: Boolean = false,
    val channel: List<ChannelUiModel> = emptyList(),
    val searchQuery: String = "",
    val error: UiText? = null,
    val isRefreshing: Boolean = false
)

sealed class FollowChannelEvent {
    object Refresh : FollowChannelEvent()
    data class ChannelClicked(val id: Int, val title: String) : FollowChannelEvent()
    data class SearchQueryChanged(val query: String) : FollowChannelEvent()
    data class FollowClicked(val id: Int) : FollowChannelEvent()
}

sealed class FollowChannelUiEvent {
    object NavigateToBack : FollowChannelUiEvent()
    data class NavigateToDetail(val id: Int, val title: String) : FollowChannelUiEvent()
    data class ShowMessage(val message: UiText) : FollowChannelUiEvent()
}
