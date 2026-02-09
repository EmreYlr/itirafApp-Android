package com.itirafapp.android.presentation.main

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.crash_report.SetUserSessionUseCase
import com.itirafapp.android.domain.usecase.navigation.GetNotificationRouteUseCase
import com.itirafapp.android.domain.usecase.navigation.HandleDeepLinkUseCase
import com.itirafapp.android.domain.usecase.theme.GetAppThemeUseCase
import com.itirafapp.android.domain.usecase.user.GetCurrentUserUseCase
import com.itirafapp.android.util.constant.ThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getAppThemeUseCase: GetAppThemeUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    setUserSessionUseCase: SetUserSessionUseCase,
    private val getNotificationRouteUseCase: GetNotificationRouteUseCase,
    private val handleDeepLinkUseCase: HandleDeepLinkUseCase
) : ViewModel() {

    init {
        val user = getCurrentUserUseCase()
        setUserSessionUseCase(user?.id)
    }
    val themeState = getAppThemeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeConfig.SYSTEM
        )
    private val _pendingRoute = MutableStateFlow<String?>(null)
    val pendingRoute = _pendingRoute.asStateFlow()

    fun handleNotificationIntent(intent: Intent?) {
        viewModelScope.launch(Dispatchers.IO) {
            intent?.extras?.let { bundle ->
                val dataMap = mutableMapOf<String, String>()

                bundle.keySet().forEach { key ->
                    @Suppress("DEPRECATION")
                    val value = bundle.get(key)
                    if (value is String || value is Number || value is Boolean) {
                        dataMap[key] = value.toString()
                    }
                }

                val route = getNotificationRouteUseCase(dataMap)

                route?.let {
                    _pendingRoute.value = it
                }
            }
        }
    }

    fun handleDeepLink(intent: Intent?) {
        viewModelScope.launch {
            val route = handleDeepLinkUseCase(intent)

            route?.let {
                _pendingRoute.value = it
            }
        }
    }

    fun clearPendingRoute() {
        _pendingRoute.value = null
    }
}