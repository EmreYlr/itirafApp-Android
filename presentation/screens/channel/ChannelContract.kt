package com.itirafapp.android.presentation.screens.channel

import com.itirafapp.android.presentation.model.ChannelUiModel

data class ChannelState(
    val isLoading: Boolean = false,
    val channel: List<ChannelUiModel> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null,
    val isRefreshing: Boolean = false
)

sealed class ChannelEvent {
    object Refresh : ChannelEvent()
    object LoadMore : ChannelEvent()
    data class ChannelClicked(val id: Int, val title: String) : ChannelEvent()
    data class SearchQueryChanged(val query: String) : ChannelEvent()
    object SearchTriggered : ChannelEvent()
    data class FollowClicked(val id: Int) : ChannelEvent()
}

sealed class ChannelUiEvent {
    data class NavigateToDetail(val id: Int, val title: String) : ChannelUiEvent()
    data class ShowMessage(val message: String) : ChannelUiEvent()
}