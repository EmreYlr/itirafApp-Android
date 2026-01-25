package com.itirafapp.android.presentation.screens.home.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.domain.usecase.confession.CreateShortlinkUseCase
import com.itirafapp.android.domain.usecase.confession.GetConfessionDetailUseCase
import com.itirafapp.android.domain.usecase.confession.LikeConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.PostReplyUseCase
import com.itirafapp.android.domain.usecase.confession.UnlikeConfessionUseCase
import com.itirafapp.android.domain.usecase.user.GetCurrentUserUseCase
import com.itirafapp.android.presentation.mapper.toUiModel
import com.itirafapp.android.presentation.model.OwnerUiModel
import com.itirafapp.android.presentation.model.ReplyUiModel
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
class DetailViewModel @Inject constructor(
    private val getConfessionDetailUseCase: GetConfessionDetailUseCase,
    private val likeConfessionUseCase: LikeConfessionUseCase,
    private val unlikeConfessionUseCase: UnlikeConfessionUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val postReplyUseCase: PostReplyUseCase,
    private val createShortlinkUseCase: CreateShortlinkUseCase,
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
                handleShareClick()
            }

            is DetailEvent.DMRequestClicked -> {

            }

            is DetailEvent.MoreClicked -> {

            }

            is DetailEvent.SendCommentClicked -> {
                sendComment()
            }

            is DetailEvent.CommentTextChanged -> {
                state = state.copy(commentText = event.text)
            }
        }
    }

    private fun handleShareClick() {
        val currentConfession = state.confession ?: return

        if (!currentConfession.shortlink.isNullOrBlank()) {
            sendUiEvent(DetailUiEvent.OpenShareSheet(currentConfession.shortlink))
        } else {
            fetchShortLink(currentConfession.id)
        }
    }

    private fun fetchShortLink(id: Int) {
        createShortlinkUseCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {}

                is Resource.Success -> {
                    val response = result.data

                    val newLink = response?.url ?: ""

                    if (newLink.isNotBlank()) {
                        val updatedConfession = state.confession?.copy(shortlink = newLink)

                        state = state.copy(
                            confession = updatedConfession
                        )

                        sendUiEvent(DetailUiEvent.OpenShareSheet(newLink))
                    } else {
                        sendUiEvent(DetailUiEvent.ShowMessage("Link boş geldi."))
                    }
                }

                is Resource.Error -> {
                    sendUiEvent(DetailUiEvent.ShowMessage(result.message ?: "Link oluşturulamadı"))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendComment() {
        val currentUserId = getCurrentUserUseCase()?.id ?: "-1"

        val currentConfession = state.confession ?: return
        val message = state.commentText.trim()
        val id = currentConfession.id

        if (message.isBlank()) return

        postReplyUseCase(id, message).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    val newReply = ReplyUiModel(
                        id = -1,
                        message = message,
                        owner = OwnerUiModel(
                            currentUserId,
                            username = UiText.StringResource(R.string.confession_owner_you)
                        ),
                        createdAt = "Şimdi"
                    )

                    val updatedReplies = currentConfession.replies + newReply

                    state = state.copy(
                        commentText = "",
                        confession = currentConfession.copy(
                            replies = updatedReplies,
                            replyCount = currentConfession.replyCount + 1
                        )
                    )

                    sendUiEvent(DetailUiEvent.ShowMessage("Yorumunuz gönderildi!"))
                }

                is Resource.Error -> {
                    sendUiEvent(DetailUiEvent.ShowMessage(result.message ?: "Yorum gönderilemedi"))
                }
            }
        }.launchIn(viewModelScope)

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