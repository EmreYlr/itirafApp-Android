package com.itirafapp.android.presentation.screens.my_confession

import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.util.state.UiText

data class MyConfessionState(
    val isLoading: Boolean = false,
    val myConfession: List<MyConfessionData> = emptyList(),
    val isRefreshing: Boolean = false,
    val isUserAdmin: Boolean = false,
    val error: UiText? = null
)

sealed class MyConfessionEvent {
    object Refresh : MyConfessionEvent()
    object LoadMore : MyConfessionEvent()
    object ModerationClicked : MyConfessionEvent()
    data class ItemClicked(val id: Int) : MyConfessionEvent()
    data class EditClicked(val id: Int) : MyConfessionEvent()
}

sealed class MyConfessionUiEvent {
    data class NavigateToDetail(val data: MyConfessionData) : MyConfessionUiEvent()
    data class NavigateToEdit(val data: MyConfessionData) : MyConfessionUiEvent()
    object NavigateToModeration : MyConfessionUiEvent()
    data class ShowMessage(val message: UiText) : MyConfessionUiEvent()
}