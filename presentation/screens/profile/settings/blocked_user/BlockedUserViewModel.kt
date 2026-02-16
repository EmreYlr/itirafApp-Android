package com.itirafapp.android.presentation.screens.profile.settings.blocked_user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.user.GetBlockedUsersUseCase
import com.itirafapp.android.domain.usecase.user.UnblockUserUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockedUserViewModel @Inject constructor(
    private val getBlockedUsersUseCase: GetBlockedUsersUseCase,
    private val unblockUserUseCase: UnblockUserUseCase
) : ViewModel() {

    var state by mutableStateOf(BlockedUserState())
        private set

    private val _uiEvent = Channel<BlockedUserUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadBlockedData(isSwipeRefresh = false)
    }

    fun onEvent(event: BlockedUserEvent) {
        when (event) {
            is BlockedUserEvent.UnblockButtonClicked -> {
                state = state.copy(
                    showUnblockDialog = true,
                    targetUserId = event.userId
                )
            }

            is BlockedUserEvent.UnblockDialogDismissed -> {
                state = state.copy(
                    showUnblockDialog = false,
                    targetUserId = null
                )
            }

            is BlockedUserEvent.UnblockConfirmed -> {
                unblockUser()
            }

            is BlockedUserEvent.Refresh -> {
                loadBlockedData(isSwipeRefresh = true)
            }
        }
    }

    private fun loadBlockedData(isSwipeRefresh: Boolean = false) {
        getBlockedUsersUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = if (isSwipeRefresh) {
                        state.copy(isRefreshing = true, isLoading = false, error = null)
                    } else {
                        state.copy(isLoading = true, isRefreshing = false, error = null)
                    }
                }

                is Resource.Success -> {
                    state = state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        blockedUsers = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = result.error.message
                    )
                    sendUiEvent(BlockedUserUiEvent.ShowMessage(result.error.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun unblockUser() {
        val userId = state.targetUserId ?: return

        state = state.copy(showUnblockDialog = false)

        unblockUserUseCase(targetUserId = userId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }

                is Resource.Success -> {
                    state = state.copy(
                        isLoading = false,
                        targetUserId = null
                    )
                    loadBlockedData(isSwipeRefresh = false)
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.error.message,
                        targetUserId = null
                    )
                    sendUiEvent(BlockedUserUiEvent.ShowMessage(result.error.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: BlockedUserUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}