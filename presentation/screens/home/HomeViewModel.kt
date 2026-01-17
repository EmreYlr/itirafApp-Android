package com.itirafapp.android.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.notification.FetchNotificationCountUseCase
import com.itirafapp.android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchNotificationCountUseCase: FetchNotificationCountUseCase,
): ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        checkNotifications()
    }

    private fun checkNotifications() {
        fetchNotificationCountUseCase().onEach {
            result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy()
                }
                is Resource.Success -> {
                    val notification = result.data
                    state = state.copy(notificationCount = notification?.count ?: 0)
                    state = state.copy(hasUnread = notification?.hasUnread ?: false)
                }
                is Resource.Error -> {
                    state = state.copy()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.UpdateNotificationCount -> {
                state = state.copy(notificationCount = event.count)
            }
            is HomeEvent.UpdateNotificationStatus -> {
                state = state.copy(hasUnread = event.status)
            }

            is HomeEvent.ConfessionClicked -> {
                sendUiEvent(HomeUiEvent.NavigateToConfessionDetail)
            }

            is HomeEvent.NotificationClicked -> {
                sendUiEvent(HomeUiEvent.NavigateToNotifications)
            }
        }
    }

    private fun sendUiEvent(event: HomeUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}