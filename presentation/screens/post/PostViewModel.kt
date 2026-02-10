package com.itirafapp.android.presentation.screens.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.domain.usecase.confession.PostConfessionUseCase
import com.itirafapp.android.domain.usecase.follow.GetLocalFollowedChannelsUseCase
import com.itirafapp.android.presentation.screens.my_confession.my_confession_edit.MyConfessionEditUiEvent
import com.itirafapp.android.util.state.Resource
import com.itirafapp.android.util.state.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getLocalFollowedChannelsUseCase: GetLocalFollowedChannelsUseCase,
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
        getLocalFollowedChannelsUseCase()
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
            sendUiEvent(
                PostUiEvent.ShowMessage(
                    UiText.StringResource(R.string.validation_error_select_channel)
                )
            )
            return
        }

        if (message.isBlank()) {
            val error = AppError.ValidationError.EmptyField(
                fieldName = UiText.StringResource(R.string.label_confession_text)
            )
            sendUiEvent(
                PostUiEvent.ShowMessage(
                    message = error.message,
                )
            )
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
                        sendUiEvent(
                            PostUiEvent.ShowMessage(
                                UiText.StringResource(R.string.message_confession_sent_moderation)
                            )
                        )
                        sendUiEvent(PostUiEvent.Dismiss)
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.error.message
                        )
                        sendUiEvent(PostUiEvent.ShowMessage(result.error.message))
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: PostUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}