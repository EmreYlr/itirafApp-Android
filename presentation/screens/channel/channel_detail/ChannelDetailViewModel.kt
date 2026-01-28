package com.itirafapp.android.presentation.screens.channel.channel_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.domain.usecase.confession.GetChannelConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.LikeConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.UnlikeConfessionUseCase
import com.itirafapp.android.domain.usecase.follow.ToggleFollowChannelUseCase
import com.itirafapp.android.domain.usecase.user.GetCurrentUserUseCase
import com.itirafapp.android.domain.usecase.user.IsUserAuthenticatedUseCase
import com.itirafapp.android.presentation.mapper.toUiModel
import com.itirafapp.android.presentation.model.ConfessionUiModel
import com.itirafapp.android.presentation.model.toggleLikeState
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChannelDetailViewModel @Inject constructor(
    private val getChannelConfessionUseCase: GetChannelConfessionUseCase,
    private val followRepository: FollowRepository,
    private val toggleFollowChannelUseCase: ToggleFollowChannelUseCase,
    private val likeConfessionUseCase: LikeConfessionUseCase,
    private val unlikeConfessionUseCase: UnlikeConfessionUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var state by mutableStateOf(ChannelDetailState())
        private set

    private val _uiEvent = Channel<ChannelDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val channelId: Int = checkNotNull(savedStateHandle.get<Int>("channelId"))
    private val channelTitle: String = checkNotNull(savedStateHandle.get<String>("channelTitle"))

    private var currentPage = 1
    private var isLastPage = false

    init {
        state = state.copy(
            channelId = channelId,
            channelTitle = channelTitle
        )
        observeFollowStatus()
        loadConfessions()
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val isAuthenticated = isUserAuthenticatedUseCase()
        state = state.copy(isUserAuthenticated = isAuthenticated)
    }

    private fun observeFollowStatus() {
        followRepository.followedChannels
            .map { list -> list.any { it.id == channelId } }
            .distinctUntilChanged()
            .onEach { isFollowing ->
                state = state.copy(isFollowing = isFollowing)
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: ChannelDetailEvent) {
        when (event) {
            is ChannelDetailEvent.ToggleFollow -> toggleFollow()

            is ChannelDetailEvent.Refresh -> {
                currentPage = 1
                isLastPage = false
                loadConfessions(isRefresh = true)
            }

            is ChannelDetailEvent.LoadMore -> {
                if (!state.isLoading && !isLastPage) {
                    loadConfessions()
                }
            }

            is ChannelDetailEvent.AddPostClicked -> {
                sendUiEvent(ChannelDetailUiEvent.NavigateToAddPost(channelId))
            }

            is ChannelDetailEvent.BackClicked -> {
                sendUiEvent(ChannelDetailUiEvent.NavigateBack)
            }

            is ChannelDetailEvent.ConfessionClicked -> {
                sendUiEvent(ChannelDetailUiEvent.NavigateToConfessionDetail(event.id))
            }

            is ChannelDetailEvent.LikeConfession -> toggleLike(event.id)
        }
    }

    private fun loadConfessions(isRefresh: Boolean = false) {
        val currentUserId = getCurrentUserUseCase()?.id

        getChannelConfessionUseCase(id = channelId, page = currentPage)
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
                            if (!isLastPage) currentPage++

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
                            error = result.message
                        )
                        sendUiEvent(
                            ChannelDetailUiEvent.ShowMessage(
                                result.message ?: "Hata oluştu"
                            )
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun toggleFollow() {
        viewModelScope.launch {
            val channelData = ChannelData(
                id = channelId,
                title = channelTitle,
                description = "",
                imageURL = null
            )
            val result = toggleFollowChannelUseCase(channelData)

            if (result is Resource.Error) {
                sendUiEvent(ChannelDetailUiEvent.ShowMessage(result.message ?: "İşlem başarısız"))
            }
        }
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
                sendUiEvent(ChannelDetailUiEvent.ShowMessage(result.message ?: "İşlem başarısız"))
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

    private fun sendUiEvent(event: ChannelDetailUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}