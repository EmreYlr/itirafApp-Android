package com.itirafapp.android.presentation.screens.home.dm_request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.room.RequestCreateRoomUseCase
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DMRequestViewModel @Inject constructor(
    private val requestCreateRoomUseCase: RequestCreateRoomUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val postId: String? = savedStateHandle.get<String>("postId")

    var state by mutableStateOf(DMRequestState())
        private set

    private val _uiEvent = Channel<DMRequestUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: DMRequestEvent) {
        when (event) {
            is DMRequestEvent.SubmitClicked -> {
                sendRequest()
            }

            is DMRequestEvent.MessageChanged -> {
                state = state.copy(initialMessage = event.message)
            }

            is DMRequestEvent.ShareLinksToggled -> {
                state = state.copy(shareSocialLinks = event.isChecked)
            }
        }
    }

    private fun sendRequest() {
        val id = postId?.toIntOrNull()

        if (id == null) {
            viewModelScope.launch { _uiEvent.send(DMRequestUiEvent.ShowMessage("ID Hatası")) }
            return
        }

        requestCreateRoomUseCase(
            channelMessageId = id,
            initialMessage = state.initialMessage,
            shareSocialLinks = state.shareSocialLinks
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true, error = null)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    _uiEvent.send(DMRequestUiEvent.Dismiss)
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message ?: "Beklenmedik bir hata oluştu"
                    )
                    _uiEvent.send(DMRequestUiEvent.ShowMessage(result.message ?: "Hata"))
                }
            }
        }.launchIn(viewModelScope)
    }
}
