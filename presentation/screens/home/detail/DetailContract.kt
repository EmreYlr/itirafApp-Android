package com.itirafapp.android.presentation.screens.home.detail

import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.presentation.model.ConfessionDetailUiModel
import com.itirafapp.android.util.state.ActiveDialog
import com.itirafapp.android.util.state.UiText

data class DetailState(
    val isLoading: Boolean = false,
    val confession: ConfessionDetailUiModel? = null,
    val isAdmin: Boolean = false,
    val commentText: String = "",
    val isSendingComment: Boolean = false,
    val error: UiText? = null,
    val activeDialog: ActiveDialog? = null
)

sealed class DetailEvent {
    object BackClicked : DetailEvent()
    data class LikeClicked(val id: Int) : DetailEvent()
    data class DMRequestClicked(val id: Int) : DetailEvent()
    data class ShareClicked(val id: Int) : DetailEvent()
    data class CommentTextChanged(val text: String) : DetailEvent()
    object SendCommentClicked : DetailEvent()
    data class DeleteItemClicked(val id: Int, val isReply: Boolean) : DetailEvent()
    data class ReportItemClicked(val id: Int, val isReply: Boolean) : DetailEvent()
    data class BlockUserClicked(val id: String, val isReply: Boolean) : DetailEvent()
    data class AdminClicked(val id: Int, val isNsfw: Boolean) : DetailEvent()
    object DismissDialog : DetailEvent()
    object ConfirmAction : DetailEvent()
}
sealed class DetailUiEvent {
    object NavigateToBack : DetailUiEvent()
    data class OpenShareSheet(val link: String) : DetailUiEvent()
    data class OpenReportSheet(val target: ReportTarget) : DetailUiEvent()
    data class OpenDMSheet(val targetId: Int) : DetailUiEvent()
    data class OpenAdminSheet(val targetId: Int, val isNsfw: Boolean) : DetailUiEvent()
    data class ShowMessage(val message: UiText) : DetailUiEvent()
}