package com.itirafapp.android.presentation.screens.moderation

import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.enums.ModerationFilter
import com.itirafapp.android.util.state.UiText

data class ModerationState(
    val isLoading: Boolean = false,
    val moderationData: List<ModerationData> = emptyList(),
    val selectedFilter: ModerationFilter = ModerationFilter.ALL,
    val isRefreshing: Boolean = false,
    val error: UiText? = null
)

sealed class ModerationEvent {
    object Refresh : ModerationEvent()
    object LoadMore : ModerationEvent()
    object LoadData : ModerationEvent()
    data class ItemClicked(val id: Int) : ModerationEvent()
    data class FilterChanged(val filter: ModerationFilter) : ModerationEvent()
}

sealed class ModerationUiEvent {
    data class NavigateToDetail(val data: ModerationData) : ModerationUiEvent()
    data class ShowMessage(val message: UiText) : ModerationUiEvent()
}