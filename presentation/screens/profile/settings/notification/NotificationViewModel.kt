package com.itirafapp.android.presentation.screens.profile.settings.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.NotificationItemUpdate
import com.itirafapp.android.domain.model.NotificationPreferences
import com.itirafapp.android.domain.model.NotificationPreferencesUpdate
import com.itirafapp.android.domain.model.enums.NotificationChannelType
import com.itirafapp.android.domain.usecase.device.RegisterDeviceUseCase
import com.itirafapp.android.domain.usecase.notification.CheckNotificationPermissionUseCase
import com.itirafapp.android.domain.usecase.notification.FetchNotificationPreferencesUseCase
import com.itirafapp.android.domain.usecase.notification.UpdateNotificationPreferencesUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val fetchNotificationPreferencesUseCase: FetchNotificationPreferencesUseCase,
    private val updateNotificationPreferencesUseCase: UpdateNotificationPreferencesUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val checkNotificationPermissionUseCase: CheckNotificationPermissionUseCase,
    private val notificationMenuProvider: NotificationMenuProvider,
) : ViewModel() {

    var state by mutableStateOf(NotificationState())
        private set

    private val _uiEvent = Channel<NotificationUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var initialPreferences: NotificationPreferences? = null

    init {
        showDefaultData()
        fetchNotificationPreferences()
        syncWithSystemPermission()
    }

    private fun showDefaultData() {
        val defaultItems = notificationMenuProvider.getMenu()
        state = state.copy(
            notificationItems = defaultItems,
            isMasterEnabled = true
        )
    }

    private fun fetchNotificationPreferences() {
        fetchNotificationPreferencesUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> state = state.copy(isLoading = true)
                is Resource.Success -> {
                    result.data?.let { preferences ->
                        val hasPermission = checkNotificationPermissionUseCase()

                        val effectivePreferences = if (!hasPermission) {
                            preferences.copy(pushEnabled = false)
                        } else {
                            preferences
                        }

                        initialPreferences = effectivePreferences
                        updateUiStateWithApiData(effectivePreferences)

                    } ?: run {
                        state = state.copy(isLoading = false)
                    }
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(NotificationUiEvent.ShowMessage(result.message ?: "Hata"))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun syncWithSystemPermission() {
        val hasSystemPermission = checkNotificationPermissionUseCase()

        if (!hasSystemPermission && state.isMasterEnabled) {
            state = state.copy(isMasterEnabled = false)
        }
    }

    private fun updateUiStateWithApiData(preferences: NotificationPreferences) {
        val masterEnabled = preferences.pushEnabled
        val defaultUiItems = notificationMenuProvider.getMenu()
        val apiPushItems = preferences.items.filter {
            it.notificationType == NotificationChannelType.PUSH
        }
        val mergedItems = defaultUiItems.map { uiItem ->
            val matchingApiItem = apiPushItems.find { it.eventType == uiItem.type }

            if (matchingApiItem != null) {
                uiItem.copy(isEnabled = matchingApiItem.enabled)
            } else {
                uiItem.copy(isEnabled = false)
            }
        }

        state = state.copy(
            isLoading = false,
            isMasterEnabled = masterEnabled,
            notificationItems = mergedItems
        )
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.OnBackClicked -> {
                saveChangesAndExit()
            }

            is NotificationEvent.OnMasterSwitchChanged -> {
                if (event.isEnabled) {
                    if (checkNotificationPermissionUseCase()) {
                        state = state.copy(isMasterEnabled = true)
                    } else {
                        sendUiEvent(NotificationUiEvent.RequestSystemPermission("Bildirim ayarlarına erişmek için izin vermelisiniz."))
                    }
                } else {
                    state = state.copy(isMasterEnabled = false)
                }
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

    private fun saveChangesAndExit() {
        updateDeviceRegistrationStatus()

        val initial = initialPreferences ?: return sendUiEvent(NotificationUiEvent.NavigateToBack)

        val pushUpdate = state.isMasterEnabled.takeIf { it != initial.pushEnabled }

        val itemsUpdate = getItemsUpdateIfChanged(initial)

        if (pushUpdate == null && itemsUpdate == null) {
            sendUiEvent(NotificationUiEvent.NavigateToBack)
            return
        }

        sendUiEvent(NotificationUiEvent.NavigateToBack)

        val updateModel = NotificationPreferencesUpdate(
            pushEnabled = pushUpdate,
            items = itemsUpdate
        )

        updateNotificationPreferences(updateModel)
    }

    private fun updateDeviceRegistrationStatus() {
        val currentMasterStatus = state.isMasterEnabled
        viewModelScope.launch {
            withContext(NonCancellable) {
                registerDeviceUseCase(pushEnabled = currentMasterStatus)
            }
        }
    }

    private fun getItemsUpdateIfChanged(initial: NotificationPreferences): List<NotificationItemUpdate>? {
        val initialPushItems =
            initial.items.filter { it.notificationType == NotificationChannelType.PUSH }

        val hasChanges = state.notificationItems.any { uiItem ->
            val initialItem = initialPushItems.find { it.eventType == uiItem.type }
            initialItem?.enabled != uiItem.isEnabled
        }

        return if (hasChanges) {
            state.notificationItems.map { NotificationItemUpdate(it.type, it.isEnabled) }
        } else null
    }

    private fun updateNotificationPreferences(updateModel: NotificationPreferencesUpdate) {
        viewModelScope.launch {
            withContext(NonCancellable) {
                updateNotificationPreferencesUseCase(updateModel).collect { result ->
                    if (result is Resource.Error) {
                        println("Bildirim ayarları güncellenirken hata: ${result.message}")
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: NotificationUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}