package com.itirafapp.android.presentation.screens.my_confession.my_confession_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.Owner
import com.itirafapp.android.domain.model.Reply
import com.itirafapp.android.domain.usecase.confession.CreateShortlinkUseCase
import com.itirafapp.android.domain.usecase.confession.LikeConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.PostReplyUseCase
import com.itirafapp.android.domain.usecase.confession.UnlikeConfessionUseCase
import com.itirafapp.android.domain.usecase.user.GetCurrentUserUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyConfessionDetailViewModel @Inject constructor(
    private val likeConfessionUseCase: LikeConfessionUseCase,
    private val unlikeConfessionUseCase: UnlikeConfessionUseCase,
    private val postReplyUseCase: PostReplyUseCase,
    private val createShortlinkUseCase: CreateShortlinkUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    var state by mutableStateOf(MyConfessionDetailState(confessions = null))
        private set

    private val _uiEvent = Channel<MyConfessionDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val userId = getCurrentUserUseCase()?.id
        state = state.copy(currentUserId = userId)
    }

    fun setInitialData(data: MyConfessionData) {
        if (state.confessions == null) {
            state = state.copy(confessions = data)
        }
    }

    fun onEvent(event: MyConfessionDetailEvent) {
        when (event) {
            is MyConfessionDetailEvent.BackClicked -> {
                sendUiEvent(MyConfessionDetailUiEvent.NavigateToBack)
            }

            is MyConfessionDetailEvent.LikeClicked -> {
                toggleLike()
            }

            is MyConfessionDetailEvent.ShareClicked -> {
                handleShareClick()
            }

            is MyConfessionDetailEvent.CommentTextChanged -> {
                state = state.copy(commentText = event.text)
            }

            is MyConfessionDetailEvent.SendCommentClicked -> {
                sendComment()
            }
        }
    }

    private fun toggleLike() {
        val currentConfession = state.confessions ?: return
        val wasLiked = currentConfession.isLiked
        val id = currentConfession.id

        val updatedConfession = currentConfession.copy(
            isLiked = !wasLiked,
            likeCount = if (wasLiked) currentConfession.likeCount - 1 else currentConfession.likeCount + 1
        )
        state = state.copy(confessions = updatedConfession)

        viewModelScope.launch {
            val result = if (wasLiked) {
                unlikeConfessionUseCase(id)
            } else {
                likeConfessionUseCase(id)
            }

            if (result is Resource.Error) {
                state = state.copy(confessions = currentConfession)
                sendUiEvent(
                    MyConfessionDetailUiEvent.ShowMessage(
                        result.message ?: "İşlem başarısız"
                    )
                )
            }
        }
    }

    private fun handleShareClick() {
        val currentConfession = state.confessions ?: return
        createShortlink(currentConfession.id)
    }

    private fun createShortlink(id: Int) {
        createShortlinkUseCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    val link = result.data?.url
                    if (!link.isNullOrBlank()) {
                        sendUiEvent(MyConfessionDetailUiEvent.OpenShareSheet(link))
                    } else {
                        sendUiEvent(MyConfessionDetailUiEvent.ShowMessage("Link oluşturulamadı"))
                    }
                }

                is Resource.Error -> {
                    sendUiEvent(
                        MyConfessionDetailUiEvent.ShowMessage(
                            result.message ?: "Hata oluştu"
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendComment() {
        val currentConfession = state.confessions ?: return
        val message = state.commentText.trim()
        val id = currentConfession.id

        if (message.isBlank()) return

        val currentUser = getCurrentUserUseCase()
        val currentUserId = currentUser?.id ?: "-1"
        val currentUserName = currentUser?.username ?: "Ben"

        postReplyUseCase(id, message).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }

                is Resource.Success -> {
                    val newOwner = Owner(
                        id = currentUserId,
                        username = currentUserName
                    )

                    val newReply = Reply(
                        id = -1,
                        message = message,
                        owner = newOwner,
                        createdAt = "Şimdi"
                    )

                    val updatedReplies =
                        currentConfession.reply + newReply

                    state = state.copy(
                        isLoading = false,
                        commentText = "",
                        confessions = currentConfession.copy(
                            reply = updatedReplies,
                            replyCount = currentConfession.replyCount + 1
                        )
                    )

                    sendUiEvent(MyConfessionDetailUiEvent.ShowMessage("Yorum gönderildi"))
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(
                        MyConfessionDetailUiEvent.ShowMessage(
                            result.message ?: "Yorum gönderilemedi"
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: MyConfessionDetailUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}