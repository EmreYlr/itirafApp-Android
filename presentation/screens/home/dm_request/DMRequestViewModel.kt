package com.itirafapp.android.presentation.screens.home.dm_request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.domain.usecase.room.RequestCreateRoomUseCase
import com.itirafapp.android.util.state.Resource
import com.itirafapp.android.util.state.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DMRequestViewModel @Inject constructor(
    private val requestCreateRoomUseCase: RequestCreateRoomUseCase,
) : ViewModel() {
    private var postId: Int? = null

    var state by mutableStateOf(DMRequestState())
        private set

    private val _uiEvent = Channel<DMRequestUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: DMRequestEvent) {
        when (event) {
            is DMRequestEvent.Init -> {
                postId = event.postId

                state = state.copy(
                    initialMessage = "",
                    error = null,
                    shareSocialLinks = true,
                    isLoading = false
                )
            }

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
        val currentId = postId

        if (currentId == null) {
            viewModelScope.launch { _uiEvent.send(DMRequestUiEvent.ShowMessage(AppError.BusinessError.RequestSentIdNotFound.message)) }
            return
        }

        requestCreateRoomUseCase(
            channelMessageId = currentId,
            initialMessage = state.initialMessage,
            shareSocialLinks = state.shareSocialLinks
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true, error = null)
                }

                is Resource.Success -> {
                    state = state.copy(
                        isLoading = false,
                        error = null,
                        initialMessage = "",
                        shareSocialLinks = true
                    )

                    _uiEvent.send(
                        DMRequestUiEvent.ShowMessage(
                            UiText.StringResource(R.string.message_dm_request_sent)
                        )
                    )
                    _uiEvent.send(DMRequestUiEvent.Dismiss)
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                    _uiEvent.send(DMRequestUiEvent.ShowMessage(result.error.message))
                }
            }
        }.launchIn(viewModelScope)
    }
}
