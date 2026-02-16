package com.itirafapp.android.presentation.screens.profile.settings.blocked_user

import com.itirafapp.android.domain.model.BlockedUsers
import com.itirafapp.android.util.state.UiText

data class BlockedUserState(
    val blockedUsers: List<BlockedUsers> = emptyList(),
    val showUnblockDialog: Boolean = false,
    val targetUserId: String? = null,
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null,
)

sealed class BlockedUserEvent {
    object Refresh : BlockedUserEvent()
    data class UnblockButtonClicked(val userId: String) : BlockedUserEvent()
    object UnblockDialogDismissed : BlockedUserEvent()
    object UnblockConfirmed : BlockedUserEvent()
}

sealed class BlockedUserUiEvent {
    data class ShowMessage(val message: UiText) : BlockedUserUiEvent()
}