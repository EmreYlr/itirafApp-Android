package com.itirafapp.android.presentation.screens.profile.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.enums.SettingActionType
import com.itirafapp.android.domain.usecase.auth.LogoutAnonymousUserUseCase
import com.itirafapp.android.domain.usecase.auth.LogoutUserUseCase
import com.itirafapp.android.domain.usecase.theme.GetAppThemeUseCase
import com.itirafapp.android.domain.usecase.theme.SetAppThemeUseCase
import com.itirafapp.android.domain.usecase.user.IsUserAuthenticatedUseCase
import com.itirafapp.android.util.constant.Constants
import com.itirafapp.android.util.constant.ThemeConfig
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val logoutAnonymousUserUseCase: LogoutAnonymousUserUseCase,
    private val isUserAuthenticated: IsUserAuthenticatedUseCase,
    private val settingsMenuProvider: SettingsMenuProvider,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val setAppThemeUseCase: SetAppThemeUseCase
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

    private val _uiEvent = Channel<SettingsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        initializeSettings()
        observeTheme()
    }

    private fun initializeSettings() {
        val isRegisteredUser = isUserAuthenticated.invoke()
        val isAnonymous = !isRegisteredUser

        val menuItems = settingsMenuProvider.getMenu(isAnonymous)

        state = state.copy(
            sections = menuItems,
            isAnonymous = isAnonymous
        )
    }

    private fun observeTheme() {
        getAppThemeUseCase().onEach { theme ->
            state = state.copy(currentTheme = theme)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ItemClicked -> {
                handleItemAction(event.action)
            }

            is SettingsEvent.OnBackClicked -> {
                sendUiEvent(SettingsUiEvent.NavigateToBack)
            }

            is SettingsEvent.DismissThemeDialog -> {
                state = state.copy(showThemeDialog = false)
            }

            is SettingsEvent.ThemeSelected -> {
                updateTheme(event.theme)
            }

            is SettingsEvent.LogoutClicked -> {
                logout()
            }
        }
    }

    private fun updateTheme(theme: ThemeConfig) {
        viewModelScope.launch {
            setAppThemeUseCase(theme)
            state = state.copy(showThemeDialog = false)
        }
    }

    private fun handleItemAction(action: SettingActionType) {
        when (action) {
            SettingActionType.EDIT_PROFILE -> {
                sendUiEvent(SettingsUiEvent.NavigateToEdit)
            }

            SettingActionType.NOTIFICATIONS -> {
                sendUiEvent(SettingsUiEvent.NavigateToNotification)
            }

            SettingActionType.THEME -> {
                state = state.copy(showThemeDialog = true)
            }

            SettingActionType.LANGUAGE -> {
                //TODO: Language
            }

            SettingActionType.RULES -> openUrl(Constants.RULES_URL)
            SettingActionType.PRIVACY_POLICY -> openUrl(Constants.PRIVACY_URL)
            SettingActionType.USER_AGREEMENT -> openUrl(Constants.TERMS_URL)
            SettingActionType.HELP_CENTER -> {
                openUrl(Constants.WEBSITE_URL)
            }

            SettingActionType.CONTACT_INFO -> {
                sendUiEvent(
                    SettingsUiEvent.CopyToClipboard(
                        text = Constants.INFO_MAIL,
                        message = "Mail adresi kopyalandı"
                    )
                )
            }
        }
    }

    private fun logout() {
        if (state.isAnonymous) {
            viewModelScope.launch {
                logoutAnonymousUserUseCase()
                sendUiEvent(SettingsUiEvent.NavigateToLogin)
            }
            return
        }

        logoutUserUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(SettingsUiEvent.NavigateToLogin)
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(SettingsUiEvent.ShowMessage(result.message ?: "Çıkış yapılamadı"))
                    sendUiEvent(SettingsUiEvent.NavigateToLogin)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun openUrl(url: String) {
        sendUiEvent(SettingsUiEvent.NavigateToUrl(url))
    }

    private fun sendUiEvent(event: SettingsUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}