package com.itirafapp.android.presentation.screens.home.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.confession.GetConfessionDetailUseCase
import com.itirafapp.android.domain.usecase.confession.LikeConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.UnlikeConfessionUseCase
import com.itirafapp.android.domain.usecase.user.GetCurrentUserUseCase
import com.itirafapp.android.presentation.mapper.toUiModel
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getConfessionDetailUseCase: GetConfessionDetailUseCase,
    private val likeConfessionUseCase: LikeConfessionUseCase,
    private val unlikeConfessionUseCase: UnlikeConfessionUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val postId: String? = savedStateHandle.get<String>("postId")

    var state by mutableStateOf(DetailState())
        private set

    private val _uiEvent = Channel<DetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val id = postId?.toIntOrNull()

        if (id != null) {
            loadConfessionDetail(id)
        } else {
            state = state.copy(error = "Geçersiz veya eksik ID")
        }
    }

    private fun loadConfessionDetail(id: Int) {
        val currentUserId = getCurrentUserUseCase()?.id

        getConfessionDetailUseCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true, error = null)
                }

                is Resource.Success -> {
                    val data = result.data
                    state = if (data != null) {
                        state.copy(
                            isLoading = false,
                            confession = data.toUiModel(currentUserId)
                        )
                    } else {
                        state.copy(isLoading = false, error = "Veri boş geldi")
                    }
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message ?: "Beklenmedik bir hata oluştu"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.BackClicked -> {
                sendUiEvent(DetailUiEvent.NavigateToBack)
            }

            is DetailEvent.LikeClicked -> {
                toggleLike()
            }

            is DetailEvent.ShareClicked -> {

            }

            is DetailEvent.CommentClicked -> {
            }

            is DetailEvent.DMRequestClicked -> {

            }
        }
    }

    private fun toggleLike() {
        val currentConfession = state.confession ?: return
        val wasLiked = currentConfession.liked
        val id = currentConfession.id

        state = state.copy(
            confession = currentConfession.copy(
                liked = !wasLiked,
                likeCount = if (wasLiked) currentConfession.likeCount - 1 else currentConfession.likeCount + 1
            )
        )

        viewModelScope.launch {
            val result = if (wasLiked) {
                unlikeConfessionUseCase(id)
            } else {
                likeConfessionUseCase(id)
            }

            if (result is Resource.Error) {
                state = state.copy(confession = currentConfession)
            }
        }
    }

    private fun sendUiEvent(event: DetailUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}