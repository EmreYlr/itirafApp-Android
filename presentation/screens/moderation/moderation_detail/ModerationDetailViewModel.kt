package com.itirafapp.android.presentation.screens.moderation.moderation_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.data.remote.moderation.dto.ModerationDecision
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.usecase.moderation.PostModerationMessageUseCase
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
class ModerationDetailViewModel @Inject constructor(
    private val postModerationMessageUseCase: PostModerationMessageUseCase
) : ViewModel() {

    var state by mutableStateOf(ModerationDetailState())
        private set

    private val _uiEvent = Channel<ModerationDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setInitialData(data: ModerationData) {
        if (state.moderationData == null) {
            state = state.copy(
                moderationData = data
            )
        }
    }

    fun onEvent(event: ModerationDetailEvent) {
        when (event) {
            is ModerationDetailEvent.ChangeDecisionMode -> {
                state = state.copy(decisionMode = event.mode)
            }

            is ModerationDetailEvent.EnteredNote -> {
                state = state.copy(note = event.value)
            }

            is ModerationDetailEvent.ToggleNsfw -> {
                state = state.copy(isNsfw = event.isNsfw)
            }

            is ModerationDetailEvent.EnteredRejectionReason -> {
                state = state.copy(rejectionReason = event.value)
            }

            is ModerationDetailEvent.ToggleViolationDropdown -> {
                state = state.copy(isViolationDropdownExpanded = !state.isViolationDropdownExpanded)
            }

            is ModerationDetailEvent.DismissViolationDropdown -> {
                state = state.copy(isViolationDropdownExpanded = false)
            }

            is ModerationDetailEvent.SelectViolation -> {
                state = state.copy(
                    selectedViolation = event.violation,
                    isViolationDropdownExpanded = false
                )
            }

            is ModerationDetailEvent.Submit -> {
                submitDecision()
            }
        }
    }

    private fun submitDecision() {
        val data = state.moderationData ?: return

        if (state.decisionMode == DecisionMode.REJECT
            && state.rejectionReason.isBlank()
            && state.selectedViolation == null
        ) {
            sendUiEvent(
                ModerationDetailUiEvent.ShowMessage(
                    UiText.DynamicString("Lütfen bir ret sebebi belirtin.")
                )
            )
            return
        }

        val decision = if (state.decisionMode == DecisionMode.APPROVE) {
            ModerationDecision.APPROVE
        } else {
            ModerationDecision.REJECT
        }

        val violationsList = if (state.decisionMode == DecisionMode.REJECT) {
            state.selectedViolation?.let { listOf(it) }
        } else {
            null
        }

        val nsfwValue = if (state.decisionMode == DecisionMode.APPROVE) state.isNsfw else null
        val rejectReasonValue =
            if (state.decisionMode == DecisionMode.REJECT) state.rejectionReason else null

        postModerationMessageUseCase(
            id = data.id,
            decision = decision,
            violations = violationsList,
            rejectionReason = rejectReasonValue,
            notes = state.note,
            isNsfw = nsfwValue
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true, error = null)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(ModerationDetailUiEvent.ShowMessage(UiText.DynamicString("İşlem Başarılı")))
                    sendUiEvent(ModerationDetailUiEvent.NavigateBack)
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                    sendUiEvent(ModerationDetailUiEvent.ShowMessage(result.error.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: ModerationDetailUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}