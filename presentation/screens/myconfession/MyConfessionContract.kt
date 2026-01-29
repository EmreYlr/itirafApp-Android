package com.itirafapp.android.presentation.screens.myconfession

import com.itirafapp.android.domain.model.MyConfessionData

data class MyConfessionState(
    val isLoading: Boolean = false,
    val myConfession: List<MyConfessionData> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed class MyConfessionEvent {
    object Refresh : MyConfessionEvent()
    object LoadMore : MyConfessionEvent()
    data class ItemClicked(val id: Int) : MyConfessionEvent()
}

sealed class MyConfessionUiEvent {
    data class NavigateToDetail(val id: Int) : MyConfessionUiEvent()
    data class ShowMessage(val message: String) : MyConfessionUiEvent()
}