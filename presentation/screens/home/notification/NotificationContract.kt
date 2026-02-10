package com.itirafapp.android.presentation.screens.home.notification

import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.util.state.UiText

data class NotificationState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val notifications: List<NotificationItem> = emptyList(),
    val error: UiText? = null,
    val selectedIds: Set<String> = emptySet(),
    val isSelectionMode: Boolean = false,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val isRefreshing: Boolean = false,
    val showDeleteDialog: Boolean = false,
) {
    val unreadList: List<NotificationItem>
        get() = notifications.filter { !it.seen }

    val readList: List<NotificationItem>
        get() = notifications.filter { it.seen }
}

sealed class NotificationEvent {
    object Refresh : NotificationEvent()
    object LoadMore : NotificationEvent()

    data class DeleteNotification(val ids: List<String>) : NotificationEvent()
    object DeleteAllNotifications : NotificationEvent()
    object DeleteIconClicked : NotificationEvent()
    object DeleteDialogDismissed : NotificationEvent()
    data class MarkAsSeen(val ids: List<String>) : NotificationEvent()
    object MarkAllAsSeen : NotificationEvent()
    data class OnNotificationClick(val notification: NotificationItem) : NotificationEvent()
    data class ToggleSelection(val id: String) : NotificationEvent()
    object ClearSelection : NotificationEvent()
    object DeleteSelected : NotificationEvent()
    object MarkSelectedAsSeen : NotificationEvent()
}

sealed class NotificationUiEvent {
    object NavigateToBack : NotificationUiEvent()
    data class NavigateToDetail(val url: String) : NotificationUiEvent()
    data class ShowMessage(val message: UiText) : NotificationUiEvent()
}