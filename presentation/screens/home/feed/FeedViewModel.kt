package com.itirafapp.android.presentation.screens.home.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.confession.CreateShortlinkUseCase
import com.itirafapp.android.domain.usecase.confession.GetConfessionsUseCase
import com.itirafapp.android.domain.usecase.confession.LikeConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.UnlikeConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.ViewConfessionUseCase
import com.itirafapp.android.domain.usecase.user.GetCurrentUserUseCase
import com.itirafapp.android.presentation.mapper.toUiModel
import com.itirafapp.android.presentation.model.ConfessionUiModel
import com.itirafapp.android.presentation.model.toggleLikeState
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getConfessionsUseCase: GetConfessionsUseCase,
    private val likeConfessionUseCase: LikeConfessionUseCase,
    private val unlikeConfessionUseCase: UnlikeConfessionUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val createShortlinkUseCase: CreateShortlinkUseCase,
    private val viewConfessionUseCase: ViewConfessionUseCase
) : ViewModel() {

    var state by mutableStateOf(FeedState())
        private set

    private val _uiEvent = Channel<FeedUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentPage = 1
    private var isLastPage = false

    private val viewedConfessionIds = mutableSetOf<Int>()
    private val pendingViewIds = mutableSetOf<Int>()
    private var lastVisibleIds = emptyList<Int>()
    private var viewDebounceJob: Job? = null

    companion object {
        private const val VIEW_FLUSH_THRESHOLD = 15
        private const val VIEW_DEBOUNCE_MS = 2000L
    }

    init {
        loadConfessions()
    }

    fun onEvent(event: FeedEvent) {
        when (event) {
            is FeedEvent.Refresh -> {
                currentPage = 1
                isLastPage = false
                loadConfessions(isRefresh = true)
            }

            is FeedEvent.LoadMore -> {
                if (!state.isLoading && !isLastPage) {
                    loadConfessions()
                }
            }

            is FeedEvent.PostClicked -> sendUiEvent(FeedUiEvent.NavigateToDetail(event.id))
            is FeedEvent.LikeClicked -> toggleLike(event.id)

            is FeedEvent.DMRequestClicked -> {
                sendUiEvent(FeedUiEvent.OpenDMSheet(event.id))
            }

            is FeedEvent.ShareClicked -> {
                handleShareClick(event.id)
            }

            is FeedEvent.ChannelClicked -> {
                sendUiEvent(FeedUiEvent.NavigateToChannel(event.id, event.title))
            }

            is FeedEvent.CommentClicked -> sendUiEvent(FeedUiEvent.NavigateToDetail(event.id))

            is FeedEvent.OnVisibleItemsChanged -> {
                lastVisibleIds = event.visibleIds
                markConfessionsAsViewed(event.visibleIds)
            }

            is FeedEvent.OnScreenExit -> {
                markConfessionsAsViewed(lastVisibleIds)
            }
        }
    }

    private fun loadConfessions(isRefresh: Boolean = false) {
        val currentUserId = getCurrentUserUseCase()?.id

        getConfessionsUseCase(page = currentPage)
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
                            val newUiItems =
                                paginatedResult.items.map { it.toUiModel(currentUserId) }

                            val currentList = if (isRefresh) emptyList() else state.confessions
                            val combinedList = currentList + newUiItems

                            isLastPage = !paginatedResult.hasNextPage
                            if (!isLastPage) {
                                currentPage++
                            }

                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                confessions = combinedList
                            )
                        } else {
                            state = state.copy(isLoading = false, isRefreshing = false)
                        }
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.error.message
                        )
                        sendUiEvent(FeedUiEvent.ShowMessage(result.error.message))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleShareClick(id: Int) {
        val confession = state.confessions.find { it.id == id } ?: return
        if (!confession.shortlink.isNullOrBlank()) {
            sendUiEvent(FeedUiEvent.OpenShareSheet(confession.shortlink))
        } else {
            fetchShortLink(id)
        }
    }

    private fun fetchShortLink(id: Int) {
        createShortlinkUseCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    val response = result.data
                    val newLink = response?.url ?: ""

                    if (newLink.isNotBlank()) {
                        updateConfessionById(id) { it.copy(shortlink = newLink) }

                        sendUiEvent(FeedUiEvent.OpenShareSheet(newLink))
                    }
                }

                is Resource.Error -> {
                    sendUiEvent(FeedUiEvent.ShowMessage(result.error.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun toggleLike(id: Int) {
        val currentConfession = state.confessions.find { it.id == id } ?: return
        val oldList = state.confessions

        updateConfessionById(id) { it.toggleLikeState() }

        viewModelScope.launch {
            val result = if (currentConfession.liked) {
                unlikeConfessionUseCase(id)
            } else {
                likeConfessionUseCase(id)
            }

            if (result is Resource.Error) {
                state = state.copy(confessions = oldList)
            }
        }
    }

    private inline fun updateConfessionById(
        id: Int,
        transform: (ConfessionUiModel) -> ConfessionUiModel
    ) {
        state = state.copy(
            confessions = state.confessions.map { if (it.id == id) transform(it) else it }
        )
    }

    private fun sendUiEvent(event: FeedUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun markConfessionsAsViewed(ids: List<Int>) {
        val newIds = ids.filter { it !in viewedConfessionIds }
        if (newIds.isEmpty()) return
        pendingViewIds.addAll(newIds)

        if (pendingViewIds.size >= VIEW_FLUSH_THRESHOLD) {
            flushViewedConfessions()
            return
        }

        viewDebounceJob?.cancel()
        viewDebounceJob = viewModelScope.launch {
            delay(VIEW_DEBOUNCE_MS)
            flushViewedConfessions()
        }
    }

    private fun flushViewedConfessions() {
        viewDebounceJob?.cancel()
        val idsToSend = pendingViewIds.toList()
        if (idsToSend.isEmpty()) return
        viewedConfessionIds.addAll(idsToSend)
        pendingViewIds.clear()
        viewModelScope.launch {
            try {
                viewConfessionUseCase(idsToSend)
            } catch (_: Exception) {
            }
        }
    }

    override fun onCleared() {
        flushViewedConfessions()
        super.onCleared()
    }
}