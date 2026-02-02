package com.itirafapp.android.presentation.screens.profile.settings.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationMenuProvider: NotificationMenuProvider,

    ) : ViewModel() {

    var state by mutableStateOf(NotificationState())
        private set

    private val _uiEvent = Channel<NotificationUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val initialItems = notificationMenuProvider.getMenu()

        state = state.copy(
            isMasterEnabled = true,
            notificationItems = initialItems
        )
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.OnBackClicked -> {
                sendUiEvent(NotificationUiEvent.NavigateToBack)
            }

            is NotificationEvent.OnMasterSwitchChanged -> {
                state = state.copy(isMasterEnabled = event.isEnabled)
            }

            is NotificationEvent.OnItemSwitchChanged -> {
                val updatedList = state.notificationItems.map { item ->
                    if (item.type == event.type) {
                        item.copy(isEnabled = event.isEnabled)
                    } else {
                        item
                    }
                }
                state = state.copy(notificationItems = updatedList)
            }
        }
    }

    private fun sendUiEvent(event: NotificationUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}