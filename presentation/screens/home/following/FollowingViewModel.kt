package com.itirafapp.android.presentation.screens.home.following

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.confession.GetFollowingConfessionsUseCase
import com.itirafapp.android.domain.usecase.confession.LikeConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.UnlikeConfessionUseCase
import com.itirafapp.android.domain.usecase.user.GetCurrentUserUseCase
import com.itirafapp.android.presentation.mapper.toUiModel
import com.itirafapp.android.presentation.model.ConfessionUiModel
import com.itirafapp.android.presentation.model.toggleLikeState
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val getFollowingConfessionsUseCase: GetFollowingConfessionsUseCase,
    private val likeConfessionUseCase: LikeConfessionUseCase,
    private val unlikeConfessionUseCase: UnlikeConfessionUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    var state by mutableStateOf(FollowingState())
        private set

    private val _uiEvent = Channel<FollowingUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadConfessions()
    }

    fun onEvent(event: FollowingEvent) {
        when (event) {
            is FollowingEvent.Refresh -> {
                currentPage = 1
                isLastPage = false
                loadConfessions(isRefresh = true)
            }

            is FollowingEvent.LoadMore -> {
                if (!state.isLoading && !isLastPage) {
                    loadConfessions()
                }
            }

            is FollowingEvent.PostClicked -> sendUiEvent(FollowingUiEvent.NavigateToDetail(event.id))
            is FollowingEvent.LikeClicked -> toggleLike(event.id)

            is FollowingEvent.ChannelClicked -> sendUiEvent(FollowingUiEvent.ShowMessage("Kanal ID: ${event.id}"))
            is FollowingEvent.CommentClicked -> sendUiEvent(FollowingUiEvent.ShowMessage("Yorumlar: ${event.id}"))
            is FollowingEvent.DMRequestClicked -> sendUiEvent(FollowingUiEvent.ShowMessage("DM İsteği: ${event.id}"))
            is FollowingEvent.ShareClicked -> sendUiEvent(FollowingUiEvent.ShowMessage("Paylaş: ${event.id}"))
        }
    }

    private fun loadConfessions(isRefresh: Boolean = false) {
        val currentUserId = getCurrentUserUseCase()?.id

        getFollowingConfessionsUseCase(page = currentPage)
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
                            error = result.message ?: "Bilinmeyen bir hata oluştu"
                        )
                        sendUiEvent(FollowingUiEvent.ShowMessage(result.message ?: "Hata"))
                    }
                }
            }
            .launchIn(viewModelScope)
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
                sendUiEvent(FollowingUiEvent.ShowMessage("İşlem başarısız"))
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

    private fun sendUiEvent(event: FollowingUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}
