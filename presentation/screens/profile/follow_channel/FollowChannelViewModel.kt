package com.itirafapp.android.presentation.screens.profile.follow_channel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.usecase.follow.GetFollowedChannelsUseCase
import com.itirafapp.android.domain.usecase.follow.ToggleFollowChannelUseCase
import com.itirafapp.android.presentation.model.ChannelUiModel
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowChannelViewModel @Inject constructor(
    private val toggleFollowChannelUseCase: ToggleFollowChannelUseCase,
    private val getFollowedChannelsUseCase: GetFollowedChannelsUseCase
) : ViewModel() {

    var state by mutableStateOf(FollowChannelState())
        private set

    private val _uiEvent = Channel<FollowChannelUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var allChannels: List<ChannelUiModel> = emptyList()

    init {
        fetchFollowedChannels()
    }

    fun onEvent(event: FollowChannelEvent) {
        when (event) {
            is FollowChannelEvent.Refresh -> {
                fetchFollowedChannels(isRefreshing = true)
            }

            is FollowChannelEvent.SearchQueryChanged -> {
                filterChannels(event.query)
            }

            is FollowChannelEvent.FollowClicked -> {
                toggleFollow(event.id)
            }

            is FollowChannelEvent.ChannelClicked -> {
                sendUiEvent(FollowChannelUiEvent.NavigateToDetail(event.id, event.title))
            }
        }
    }

    private fun fetchFollowedChannels(isRefreshing: Boolean = false) {
        getFollowedChannelsUseCase()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = !isRefreshing,
                            isRefreshing = isRefreshing
                        )
                    }

                    is Resource.Success -> {
                        val data = result.data ?: emptyList()

                        allChannels = data.map {
                            ChannelUiModel(
                                id = it.id,
                                title = it.title,
                                description = it.description,
                                imageURL = it.imageURL,
                                isFollowing = true
                            )
                        }
                        filterChannels(state.searchQuery)

                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.error.message
                        )
                        sendUiEvent(
                            FollowChannelUiEvent.ShowMessage(
                                result.error.message
                            )
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun filterChannels(query: String) {
        val filteredList = if (query.isBlank()) {
            allChannels
        } else {
            allChannels.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }

        state = state.copy(
            searchQuery = query,
            channel = filteredList
        )
    }

    private fun toggleFollow(channelId: Int) {
        val updatedAllChannels = allChannels.map {
            if (it.id == channelId) it.copy(isFollowing = !it.isFollowing) else it
        }

        val updatedVisibleChannels = state.channel.map {
            if (it.id == channelId) it.copy(isFollowing = !it.isFollowing) else it
        }

        allChannels = updatedAllChannels
        state = state.copy(channel = updatedVisibleChannels)

        viewModelScope.launch {
            val targetChannel = allChannels.find { it.id == channelId } ?: return@launch

            val domainChannel = ChannelData(
                id = targetChannel.id,
                title = targetChannel.title,
                description = targetChannel.description,
                imageURL = targetChannel.imageURL
            )

            val result = toggleFollowChannelUseCase(domainChannel)

            if (result is Resource.Error) {
                sendUiEvent(FollowChannelUiEvent.ShowMessage(AppError.LocalError.Unknown.message))

                val revertedAll = allChannels.map {
                    if (it.id == channelId) it.copy(isFollowing = !it.isFollowing) else it
                }
                val revertedVisible = state.channel.map {
                    if (it.id == channelId) it.copy(isFollowing = !it.isFollowing) else it
                }

                allChannels = revertedAll
                state = state.copy(channel = revertedVisible)
            }
        }
    }

    private fun sendUiEvent(event: FollowChannelUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}