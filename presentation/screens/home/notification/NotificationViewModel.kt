package com.itirafapp.android.presentation.screens.home.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.domain.usecase.notification.DeleteAllNotificationUseCase
import com.itirafapp.android.domain.usecase.notification.DeleteNotificationUseCase
import com.itirafapp.android.domain.usecase.notification.FetchNotificationsUseCase
import com.itirafapp.android.domain.usecase.notification.MarkAllNotificationsSeenUseCase
import com.itirafapp.android.domain.usecase.notification.MarkNotificationSeenUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
    private val markNotificationSeenUseCase: MarkNotificationSeenUseCase,
    private val markAllNotificationsSeenUseCase: MarkAllNotificationsSeenUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val deleteAllNotificationUseCase: DeleteAllNotificationUseCase
) : ViewModel() {

    var state by mutableStateOf(NotificationState())
        private set

    private val _uiEvent = Channel<NotificationUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadNotifications()
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.Refresh -> {
                currentPage = 1
                isLastPage = false
                loadNotifications(isRefresh = true)
            }

            is NotificationEvent.LoadMore -> {
                if (!state.isLoading && !state.isLoadingMore && !isLastPage) {
                    loadNotifications()
                }
            }

            is NotificationEvent.ToggleSelection -> {
                toggleSelection(event.id)
            }

            is NotificationEvent.ClearSelection -> {
                state = state.copy(
                    selectedIds = emptySet(),
                    isSelectionMode = false
                )
            }

            is NotificationEvent.DeleteSelected -> {
                if (state.selectedIds.isNotEmpty()) {
                    deleteNotifications(state.selectedIds.toList())
                    state = state.copy(selectedIds = emptySet(), isSelectionMode = false)
                }
            }

            is NotificationEvent.MarkSelectedAsSeen -> {
                if (state.selectedIds.isNotEmpty()) {
                    markAsSeen(state.selectedIds.toList())
                    state = state.copy(selectedIds = emptySet(), isSelectionMode = false)
                }
            }

            is NotificationEvent.MarkAsSeen -> markAsSeen(event.ids)
            is NotificationEvent.MarkAllAsSeen -> markAllAsSeen()
            is NotificationEvent.DeleteNotification -> deleteNotifications(event.ids)
            is NotificationEvent.DeleteAllNotifications -> deleteAllNotifications()
            is NotificationEvent.OnNotificationClick -> handleNotificationClick(event.notification)
        }
    }

    private fun loadNotifications(isRefresh: Boolean = false) {
        fetchNotificationsUseCase(page = currentPage, limit = 20)
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = !isRefresh && currentPage == 1,
                            isRefreshing = isRefresh,
                            isLoadingMore = !isRefresh && currentPage > 1,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        val paginatedResult = result.data

                        if (paginatedResult != null) {
                            val newItems = paginatedResult.items

                            val currentList = if (isRefresh) emptyList() else state.notifications
                            val combinedList = currentList + newItems

                            isLastPage = !paginatedResult.hasNextPage
                            if (!isLastPage) {
                                currentPage++
                            }

                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isLoadingMore = false,
                                notifications = combinedList
                            )
                        } else {
                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isLoadingMore = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingMore = false,
                            error = result.message ?: "Bilinmeyen hata"
                        )
                        sendUiEvent(NotificationUiEvent.ShowMessage(result.message ?: "Hata"))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun markAsSeen(ids: List<String>) {
        if (ids.isEmpty()) return

        val updatedList = state.notifications.map {
            if (it.id in ids) it.copy(seen = true) else it
        }
        state = state.copy(notifications = updatedList)

        viewModelScope.launch {
            val result = markNotificationSeenUseCase(ids)
            if (result is Resource.Error) {
                sendUiEvent(NotificationUiEvent.ShowMessage("Hata oluştu"))
            }
        }
    }

    private fun markAllAsSeen() {
        val updatedList = state.notifications.map { it.copy(seen = true) }
        state = state.copy(notifications = updatedList)

        viewModelScope.launch {
            markAllNotificationsSeenUseCase()
        }
    }

    private fun deleteNotifications(ids: List<String>) {
        val remainingList = state.notifications.filter { it.id !in ids }
        state = state.copy(notifications = remainingList)

        viewModelScope.launch {
            val result = deleteNotificationUseCase(ids)
            if (result is Resource.Error) {
                sendUiEvent(NotificationUiEvent.ShowMessage("Silinemedi"))
                loadNotifications(isRefresh = true)
            }
        }
    }

    private fun deleteAllNotifications() {
        state = state.copy(notifications = emptyList())
        viewModelScope.launch {
            val result = deleteAllNotificationUseCase()
            if (result is Resource.Error) {
                sendUiEvent(NotificationUiEvent.ShowMessage("Hata"))
                loadNotifications(isRefresh = true)
            }
        }
    }

    private fun toggleSelection(id: String) {
        val currentSelection = state.selectedIds.toMutableSet()

        if (currentSelection.contains(id)) {
            currentSelection.remove(id)
        } else {
            currentSelection.add(id)
        }
        val isSelectionMode = currentSelection.isNotEmpty()

        state = state.copy(
            selectedIds = currentSelection,
            isSelectionMode = isSelectionMode
        )
    }

    private fun handleNotificationClick(notification: NotificationItem) {
        if (!notification.seen) {
            markAsSeen(listOf(notification.id))
        }
    }

    private fun sendUiEvent(event: NotificationUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}