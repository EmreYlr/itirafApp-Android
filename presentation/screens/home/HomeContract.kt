package com.itirafapp.android.presentation.screens.home

data class HomeState(
    val selectedTabIndex: Int = 0,
    val notificationCount: Int = 0,
    val hasUnread: Boolean = false
)

sealed class HomeEvent {
    data class TabChanged(val index: Int) : HomeEvent()
    data class ConfessionClicked(val postId: String) : HomeEvent()
    object NotificationClicked : HomeEvent()

}

sealed class HomeUiEvent {
    object NavigateToNotifications : HomeUiEvent()
    data class NavigateToConfessionDetail(val postId: String) : HomeUiEvent()
}