package com.itirafapp.android.presentation.screens.my_confession.my_confession_detail

import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.util.state.ActiveDialog

data class MyConfessionDetailState(
    val isLoading: Boolean = false,
    val confessions: MyConfessionData? = null,
    val commentText: String = "",
    val currentUserId: String? = null,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val activeDialog: ActiveDialog? = null
)

sealed class MyConfessionDetailEvent {
    object BackClicked : MyConfessionDetailEvent()
    object EditClicked : MyConfessionDetailEvent()
    data class LikeClicked(val id: Int) : MyConfessionDetailEvent()
    data class ShareClicked(val id: Int) : MyConfessionDetailEvent()
    data class DeleteItemClicked(val id: Int, val isReply: Boolean) : MyConfessionDetailEvent()
    data class ReportItemClicked(val id: Int, val isReply: Boolean) : MyConfessionDetailEvent()
    data class BlockUserClicked(val userId: String, val isReply: Boolean) :
        MyConfessionDetailEvent()
    data class CommentTextChanged(val text: String) : MyConfessionDetailEvent()
    object SendCommentClicked : MyConfessionDetailEvent()
    object DismissDialog : MyConfessionDetailEvent()
    object ConfirmAction : MyConfessionDetailEvent()
}

sealed class MyConfessionDetailUiEvent {
    object NavigateToBack : MyConfessionDetailUiEvent()
    data class NavigateToEdit(val data: MyConfessionData) : MyConfessionDetailUiEvent()

    data class OpenShareSheet(val link: String) : MyConfessionDetailUiEvent()
    data class OpenReportSheet(val target: ReportTarget) : MyConfessionDetailUiEvent()
    data class ShowMessage(val message: String) : MyConfessionDetailUiEvent()
}