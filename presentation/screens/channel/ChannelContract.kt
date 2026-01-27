package com.itirafapp.android.presentation.screens.channel

data class ChannelState(
    val isLoading: Boolean = false,
    //val channel: List<ChannelUiModel> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)

sealed class ChannelEvent {
    object Refresh : ChannelEvent()
    object LoadMore : ChannelEvent()
    data class ChannelClicked(val id: Int) : ChannelEvent()
    data class FollowClicked(val id: Int) : ChannelEvent()
}

sealed class ChannelUiEvent {
    data class NavigateToDetail(val id: Int) : ChannelUiEvent()
    data class ShowMessage(val message: String) : ChannelUiEvent()
}