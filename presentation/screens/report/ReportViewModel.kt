package com.itirafapp.android.presentation.screens.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.domain.usecase.confession.ReportConfessionUseCase
import com.itirafapp.android.domain.usecase.confession.ReportReplyUseCase
import com.itirafapp.android.util.extension.refinedForBusiness
import com.itirafapp.android.util.extension.refinedForRegister
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
class ReportViewModel @Inject constructor(
    private val reportConfessionUseCase: ReportConfessionUseCase,
    private val reportReplyUseCase: ReportReplyUseCase,
    //private val reportRoomUseCase: ReportRoomUseCase //TODO: Buraya bak
) : ViewModel() {
    var state by mutableStateOf(ReportState())
        private set

    private val _uiEvent = Channel<ReportUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.Init -> {
                state = state.copy(target = event.target)
                state = ReportState(
                    target = event.target,
                    reason = "",
                    isLoading = false,
                    error = null
                )
            }

            is ReportEvent.ReasonText -> {
                state = state.copy(reason = event.reason)
            }

            is ReportEvent.SubmitClicked -> {
                submitReport()
            }
        }
    }

    private fun submitReport() {
        val target = state.target
        val reason = state.reason

        if (target == null) {
            sendUiEvent(
                ReportUiEvent.ShowMessage(
                    UiText.StringResource(R.string.error_report_content_not_found),
                )
            )
            return
        }

        if (reason.isBlank()) {
            sendUiEvent(
                ReportUiEvent.ShowMessage(
                    UiText.StringResource(R.string.validation_error_write_report_reason)
                )
            )
            return
        }

        val flow = when (target) {
            is ReportTarget.Confession -> {
                reportConfessionUseCase(id = target.confessionId, reason = reason)
            }

            is ReportTarget.Comment -> {
                reportReplyUseCase(id = target.replyId, reason = reason)
            }

            is ReportTarget.Room -> {
                //reportRoomUseCase(id = target.roomId, reason = reason)

                kotlinx.coroutines.flow.flow { }
            }
        }

        flow.onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true, error = null)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(
                        ReportUiEvent.ShowMessage(
                            UiText.StringResource(R.string.message_report_received)
                        )
                    )
                    sendUiEvent(ReportUiEvent.Dismiss)
                }

                is Resource.Error -> {
                    state = state.copy(isLoading = false, error = result.error.message)
                    val originalError = result.error

                    val refinedError = originalError.refinedForBusiness()

                    sendUiEvent(ReportUiEvent.ShowMessage(refinedError.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: ReportUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}