package com.itirafapp.android.presentation.screens.home

data class HomeState(
    val notificationCount: Int = 0,
    val hasUnread: Boolean = false
)

sealed class HomeEvent {
    data class UpdateNotificationCount(val count: Int) : HomeEvent()
    data class UpdateNotificationStatus(val status: Boolean) : HomeEvent()
    object NotificationClicked : HomeEvent()
    object ConfessionClicked : HomeEvent()
}

sealed class HomeUiEvent {
    object NavigateToNotifications : HomeUiEvent()
    object NavigateToConfessionDetail : HomeUiEvent()
}