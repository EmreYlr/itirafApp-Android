package com.itirafapp.android.presentation.screens.moderation

import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.util.state.UiText

data class ModerationState(
    val isLoading: Boolean = false,
    val moderationData: List<ModerationData> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: UiText? = null
)

sealed class ModerationEvent {
    object Refresh : ModerationEvent()
    object LoadMore : ModerationEvent()
    data class ItemClicked(val id: Int) : ModerationEvent()
}

sealed class ModerationUiEvent {
    data class NavigateToDetail(val data: ModerationData) : ModerationUiEvent()
    data class ShowMessage(val message: UiText) : ModerationUiEvent()
}