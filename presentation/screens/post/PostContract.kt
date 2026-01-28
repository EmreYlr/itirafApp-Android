package com.itirafapp.android.presentation.screens.post

import com.itirafapp.android.domain.model.ChannelData

data class PostState(
    val isLoading: Boolean = false,
    val title: String = "",
    val message: String = "",

    val followedChannel: List<ChannelData> = emptyList(),
    val selectedChannel: ChannelData? = null,
    val isChannelLocked: Boolean = false,

    val error: String? = null,
)

sealed class PostEvent {
    data class Init(val channelId: Int? = null) : PostEvent()

    data class TitleChanged(val title: String) : PostEvent()
    data class MessageChanged(val message: String) : PostEvent()

    data class ChannelSelected(val channel: ChannelData) : PostEvent()

    object SubmitClicked : PostEvent()
}

sealed class PostUiEvent {
    object Dismiss : PostUiEvent()
    data class ShowMessage(val message: String) : PostUiEvent()
}