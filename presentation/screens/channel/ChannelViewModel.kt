package com.itirafapp.android.presentation.screens.channel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    //private val getChannelsUseCase: GetChannelsUseCase,
) : ViewModel() {

    var state by mutableStateOf(ChannelState())
        private set

    private val _uiEvent = Channel<ChannelUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadChannel()
    }

    fun onEvent(event: ChannelEvent) {
        when (event) {
            is ChannelEvent.ChannelClicked -> {
                sendUiEvent(ChannelUiEvent.NavigateToDetail(event.id))
            }

            is ChannelEvent.Refresh -> {
                currentPage = 1
                isLastPage = false
                loadChannel(isRefresh = true)
            }

            is ChannelEvent.LoadMore -> {
                if (!state.isLoading && !isLastPage) {
                    loadChannel()
                }
            }

            is ChannelEvent.FollowClicked -> {
                toggleFollow(event.id)
            }
        }
    }

    private fun loadChannel(isRefresh: Boolean = false) {

    }

    private fun toggleFollow(id: Int) {

    }

    private fun sendUiEvent(event: ChannelUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}