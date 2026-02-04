package com.itirafapp.android.presentation.screens.home

data class HomeState(
    val selectedTabIndex: Int = 0,
    val notificationCount: Int = 0,
    val hasUnread: Boolean = false,
    val isUserAuthenticated: Boolean = false
)

sealed class HomeEvent {
    data class TabChanged(val index: Int) : HomeEvent()
    object NotificationClicked : HomeEvent()
    object RefreshNotifications : HomeEvent()
}

sealed class HomeUiEvent {
    object NavigateToNotifications : HomeUiEvent()

}