package com.itirafapp.android.presentation.screens.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.confession.PostConfessionUseCase
import com.itirafapp.android.domain.usecase.follow.GetFollowedChannelsUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getFollowedChannelsUseCase: GetFollowedChannelsUseCase,
    private val postConfessionUseCase: PostConfessionUseCase
) : ViewModel() {

    var state by mutableStateOf(PostState())
        private set

    private val _uiEvent = Channel<PostUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var incomingChannelId: Int? = null

    fun onEvent(event: PostEvent) {
        when (event) {
            is PostEvent.Init -> {
                incomingChannelId = event.channelId

                state = state.copy(
                    title = "",
                    message = "",
                    error = null,
                    selectedChannel = null,
                    isChannelLocked = incomingChannelId != null
                )
                loadFollowedChannels()
            }

            is PostEvent.TitleChanged -> {
                state = state.copy(title = event.title)
            }

            is PostEvent.MessageChanged -> {
                state = state.copy(message = event.message)
            }

            is PostEvent.ChannelSelected -> {
                if (!state.isChannelLocked) {
                    state = state.copy(selectedChannel = event.channel)
                }
            }

            is PostEvent.SubmitClicked -> {
                submitConfession()
            }
        }
    }

    private fun loadFollowedChannels() {
        getFollowedChannelsUseCase()
            .onEach { channels ->
                var newState = state.copy(followedChannel = channels)

                if (incomingChannelId != null) {
                    val targetChannel = channels.find { it.id == incomingChannelId }
                    if (targetChannel != null) {
                        newState = newState.copy(selectedChannel = targetChannel)
                    }
                }

                state = newState
            }.launchIn(viewModelScope)
    }

    private fun submitConfession() {
        val currentChannel = state.selectedChannel
        val message = state.message.trim()
        val title = state.title.trim()

        if (currentChannel == null) {
            sendUiEvent(PostUiEvent.ShowMessage("Lütfen bir kanal seçiniz."))
            return
        }

        if (message.isBlank()) {
            sendUiEvent(PostUiEvent.ShowMessage("İtiraf metni boş olamaz."))
            return
        }

        viewModelScope.launch {
            postConfessionUseCase(
                channelId = currentChannel.id,
                title = title.ifBlank { null },
                message = message
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = true, error = null)
                    }

                    is Resource.Success -> {
                        state = state.copy(isLoading = false)
                        sendUiEvent(PostUiEvent.ShowMessage("İtirafınız modarasyona gönderildi!"))
                        sendUiEvent(PostUiEvent.Dismiss)
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.message
                        )
                        sendUiEvent(PostUiEvent.ShowMessage(result.message ?: "Bir hata oluştu"))
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: PostUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}