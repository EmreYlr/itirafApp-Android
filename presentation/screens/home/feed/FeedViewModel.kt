package com.itirafapp.android.presentation.screens.home.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.confession.GetConfessionsUseCase
import com.itirafapp.android.domain.usecase.confession.LikeConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.UnlikeConfessionUseCase
import com.itirafapp.android.presentation.mapper.toUiModel
import com.itirafapp.android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getConfessionsUseCase: GetConfessionsUseCase,
    private val likeConfessionUseCase: LikeConfessionUseCase,
    private val unlikeConfessionUseCase: UnlikeConfessionUseCase
) : ViewModel() {

    var state by mutableStateOf(FeedState())
        private set

    private val _uiEvent = Channel<FeedUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentPage = 1
    private var isLastPage = false

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

            is FeedEvent.ChannelClicked -> sendUiEvent(FeedUiEvent.ShowMessage("Kanal ID: ${event.id}"))
            is FeedEvent.CommentClicked -> sendUiEvent(FeedUiEvent.ShowMessage("Yorumlar: ${event.id}"))
            is FeedEvent.DMRequestClicked -> sendUiEvent(FeedUiEvent.ShowMessage("DM İsteği: ${event.id}"))
            is FeedEvent.ShareClicked -> sendUiEvent(FeedUiEvent.ShowMessage("Paylaş: ${event.id}"))
        }
    }

    private fun loadConfessions(isRefresh: Boolean = false) {
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
                            val newUiItems = paginatedResult.items.map { it.toUiModel() }

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
                            error = result.message ?: "Bilinmeyen bir hata oluştu"
                        )
                        sendUiEvent(FeedUiEvent.ShowMessage(result.message ?: "Hata"))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun toggleLike(id: Int) {
        val currentConfession = state.confessions.find { it.id == id } ?: return
        val wasLiked = currentConfession.liked
        val oldList = state.confessions
        val updatedList = state.confessions.map { item ->
            if (item.id == id) {
                item.copy(
                    liked = !item.liked,
                    likeCount = if (item.liked) item.likeCount - 1 else item.likeCount + 1
                )
            } else item
        }
        state = state.copy(confessions = updatedList)

        viewModelScope.launch {
            val result = if (wasLiked) {
                unlikeConfessionUseCase(id)
            } else {
                likeConfessionUseCase(id)
            }
            if (result is Resource.Error) {
                state = state.copy(confessions = oldList)

                sendUiEvent(FeedUiEvent.ShowMessage("İşlem başarısız"))
            }
        }
    }

    private fun sendUiEvent(event: FeedUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}