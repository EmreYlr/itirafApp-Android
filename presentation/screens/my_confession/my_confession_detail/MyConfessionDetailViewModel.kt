package com.itirafapp.android.presentation.screens.my_confession.my_confession_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.Owner
import com.itirafapp.android.domain.model.Reply
import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.domain.usecase.confession.CreateShortlinkUseCase
import com.itirafapp.android.domain.usecase.confession.DeleteConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.DeleteReplyUseCase
import com.itirafapp.android.domain.usecase.confession.LikeConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.PostReplyUseCase
import com.itirafapp.android.domain.usecase.confession.UnlikeConfessionUseCase
import com.itirafapp.android.domain.usecase.user.BlockUserUseCase
import com.itirafapp.android.domain.usecase.user.GetCurrentUserUseCase
import com.itirafapp.android.domain.usecase.user.IsUserAdminUseCase
import com.itirafapp.android.util.state.ActiveDialog
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
class MyConfessionDetailViewModel @Inject constructor(
    private val likeConfessionUseCase: LikeConfessionUseCase,
    private val unlikeConfessionUseCase: UnlikeConfessionUseCase,
    private val postReplyUseCase: PostReplyUseCase,
    private val createShortlinkUseCase: CreateShortlinkUseCase,
    private val deleteConfessionUseCase: DeleteConfessionUseCase,
    private val deleteReplyUseCase: DeleteReplyUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val isUserAdminUseCase: IsUserAdminUseCase
) : ViewModel() {

    var state by mutableStateOf(MyConfessionDetailState(confessions = null))
        private set

    private val _uiEvent = Channel<MyConfessionDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        checkAuthStatus()

        val userId = getCurrentUserUseCase()?.id
        state = state.copy(currentUserId = userId)
    }

    private fun checkAuthStatus() {
        val isUserAdmin = isUserAdminUseCase()
        state = state.copy(isUserAdmin = isUserAdmin)
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

            is MyConfessionDetailEvent.EditClicked -> {
                val confession = state.confessions ?: return
                sendUiEvent(MyConfessionDetailUiEvent.NavigateToEdit(confession))
            }

            is MyConfessionDetailEvent.DeleteItemClicked -> {
                state = state.copy(
                    activeDialog = ActiveDialog.DeleteItem(
                        itemId = event.id,
                        isReply = event.isReply
                    )
                )
            }

            is MyConfessionDetailEvent.BlockUserClicked -> {
                state = state.copy(
                    activeDialog = ActiveDialog.BlockUser(
                        userId = event.userId,
                        isReply = event.isReply
                    )
                )
            }

            is MyConfessionDetailEvent.ReportItemClicked -> {
                val target = if (event.isReply) {
                    ReportTarget.Comment(replyId = event.id)
                } else {
                    ReportTarget.Confession(confessionId = event.id)
                }
                sendUiEvent(MyConfessionDetailUiEvent.OpenReportSheet(target))
            }

            is MyConfessionDetailEvent.DismissDialog -> {
                state = state.copy(activeDialog = null)
            }

            is MyConfessionDetailEvent.ConfirmAction -> {
                when (val dialog = state.activeDialog) {
                    is ActiveDialog.DeleteItem -> {
                        deleteItem(dialog.itemId, dialog.isReply)
                    }

                    is ActiveDialog.BlockUser -> {
                        blockUser(dialog.userId)
                    }

                    null -> {}
                }
                state = state.copy(activeDialog = null)
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
                        result.error.message
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
                    }
                }

                is Resource.Error -> {
                    sendUiEvent(
                        MyConfessionDetailUiEvent.ShowMessage(
                            result.error.message
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
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(
                        MyConfessionDetailUiEvent.ShowMessage(
                            result.error.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteItem(itemId: Int, isReply: Boolean) {
        if (isReply) {
            deleteReply(itemId)
        } else {
            deleteMyConfession(itemId)
        }
    }

    private fun deleteReply(itemId: Int) {
        deleteReplyUseCase(id = itemId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }

                is Resource.Success -> {
                    val currentConfession = state.confessions
                    if (currentConfession != null) {
                        val updatedReplies = currentConfession.reply.filter { it.id != itemId }

                        state = state.copy(
                            isLoading = false,
                            confessions = currentConfession.copy(
                                reply = updatedReplies,
                                replyCount = updatedReplies.size
                            )
                        )
                    } else {
                        state = state.copy(isLoading = false)
                    }
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(
                        MyConfessionDetailUiEvent.ShowMessage(
                            result.error.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteMyConfession(id: Int) {
        deleteConfessionUseCase(id = id).onEach { result ->
            when (result) {
                is Resource.Loading -> state = state.copy(isLoading = true)
                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(
                        MyConfessionDetailUiEvent.ShowMessage(
                            UiText.StringResource(R.string.confession_delete_success)
                        )
                    )
                    sendUiEvent(MyConfessionDetailUiEvent.NavigateToBack)
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(
                        MyConfessionDetailUiEvent.ShowMessage(
                            result.error.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun blockUser(userId: String) {
        blockUserUseCase(targetUserId = userId).onEach { result ->
            when (result) {
                is Resource.Loading -> state = state.copy(isLoading = true)
                is Resource.Success -> {
                    val currentConfession = state.confessions
                    if (currentConfession != null) {
                        val updatedReplies =
                            currentConfession.reply.filter { it.owner.id != userId }

                        state = state.copy(
                            isLoading = false,
                            confessions = currentConfession.copy(
                                reply = updatedReplies,
                                replyCount = updatedReplies.size
                            )
                        )
                    } else {
                        state = state.copy(isLoading = false)
                    }
                    sendUiEvent(
                        MyConfessionDetailUiEvent.ShowMessage(
                            UiText.StringResource(R.string.message_user_blocked)
                        )
                    )
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(
                        MyConfessionDetailUiEvent.ShowMessage(
                            result.error.message
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