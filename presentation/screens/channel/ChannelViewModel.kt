package com.itirafapp.android.presentation.screens.channel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.usecase.channel.GetChannelsUseCase
import com.itirafapp.android.domain.usecase.channel.SearchChannelsUseCase
import com.itirafapp.android.domain.usecase.follow.GetLocalFollowedChannelsUseCase
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
class ChannelViewModel @Inject constructor(
    private val getChannelsUseCase: GetChannelsUseCase,
    private val searchChannelsUseCase: SearchChannelsUseCase,
    private val toggleFollowChannelUseCase: ToggleFollowChannelUseCase,
    private val getLocalFollowedChannelsUseCase: GetLocalFollowedChannelsUseCase
) : ViewModel() {

    var state by mutableStateOf(ChannelState())
        private set

    private val _uiEvent = Channel<ChannelUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentPage = 1
    private var isLastPage = false

    private var rawChannelList = emptyList<ChannelData>()
    private var rawSearchResults = emptyList<ChannelData>()
    private var currentFollowedIds = emptySet<Int>()

    init {
        observeFollowedChannels()

        loadChannel()
    }

    private fun observeFollowedChannels() {
        getLocalFollowedChannelsUseCase()
            .onEach { followedList ->
                currentFollowedIds = followedList.map { it.id }.toSet()
                updateUiState()
            }
            .launchIn(viewModelScope)
    }

    private fun updateUiState() {
        val sourceList = if (state.searchQuery.isNotEmpty()) rawSearchResults else rawChannelList
        val uiList = sourceList.map { channel ->
            ChannelUiModel(
                id = channel.id,
                title = channel.title,
                description = channel.description,
                imageURL = channel.imageURL,
                isFollowing = currentFollowedIds.contains(channel.id)
            )
        }

        state = state.copy(channel = uiList)
    }

    fun onEvent(event: ChannelEvent) {
        when (event) {
            is ChannelEvent.ChannelClicked -> {
                sendUiEvent(ChannelUiEvent.NavigateToDetail(event.id, event.title))
            }

            is ChannelEvent.Refresh -> {
                if (state.searchQuery.isNotEmpty()) {
                    executeSearch(state.searchQuery)
                } else {
                    currentPage = 1
                    isLastPage = false
                    loadChannel(isRefresh = true)
                }
            }

            is ChannelEvent.LoadMore -> {
                if (!state.isLoading && !isLastPage && state.searchQuery.isEmpty()) {
                    loadChannel()
                }
            }

            is ChannelEvent.SearchQueryChanged -> {
                state = state.copy(searchQuery = event.query)

                if (event.query.isEmpty()) {
                    currentPage = 1
                    isLastPage = false
                    rawSearchResults = emptyList()
                    updateUiState()

                    if (rawChannelList.isEmpty()) {
                        loadChannel(isRefresh = false)
                    }
                }
            }

            is ChannelEvent.SearchTriggered -> {
                if (state.searchQuery.isNotBlank()) {
                    executeSearch(state.searchQuery)
                }
            }

            is ChannelEvent.FollowClicked -> {
                toggleFollow(event.id)
            }
        }
    }

    private fun loadChannel(isRefresh: Boolean = false) {
        if (state.searchQuery.isNotEmpty()) return

        getChannelsUseCase(page = currentPage)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = true,
                            isRefreshing = isRefresh,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        val paginatedResult = result.data
                        if (paginatedResult != null) {
                            val newItems = paginatedResult.items

                            val currentRaw = if (isRefresh) emptyList() else rawChannelList
                            rawChannelList = currentRaw + newItems

                            isLastPage = !paginatedResult.hasNextPage
                            if (!isLastPage) currentPage++

                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false
                            )

                            updateUiState()
                        } else {
                            state = state.copy(isLoading = false, isRefreshing = false)
                        }
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.message
                        )
                        sendUiEvent(ChannelUiEvent.ShowMessage(result.message ?: "Hata"))
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun executeSearch(query: String) {
        searchChannelsUseCase(query).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true, error = null)
                }

                is Resource.Success -> {
                    val results = result.data ?: emptyList()
                    rawSearchResults = results

                    state = state.copy(isLoading = false)
                    updateUiState()
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message
                    )
                    sendUiEvent(ChannelUiEvent.ShowMessage(result.message ?: "Arama başarısız"))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun toggleFollow(id: Int) {
        viewModelScope.launch {
            val targetChannel = rawSearchResults.find { it.id == id }
                ?: rawChannelList.find { it.id == id }

            if (targetChannel != null) {
                val result = toggleFollowChannelUseCase(targetChannel)

                if (result is Resource.Error) {
                    sendUiEvent(ChannelUiEvent.ShowMessage(result.message ?: "İşlem başarısız"))
                }
            }
        }
    }

    private fun sendUiEvent(event: ChannelUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}