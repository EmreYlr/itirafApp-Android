package com.itirafapp.android.presentation.screens.moderation.moderation_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.data.remote.moderation.dto.ModerationDecision
import com.itirafapp.android.domain.usecase.moderation.PostModerationMessageUseCase
import com.itirafapp.android.domain.usecase.moderation.ToggleModerationNsfwUseCase
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.DecisionMode
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
class ModerationEditViewModel @Inject constructor(
    private val toggleModerationNsfwUseCase: ToggleModerationNsfwUseCase,
    private val postModerationMessageUseCase: PostModerationMessageUseCase
) : ViewModel() {

    var state by mutableStateOf(ModerationEditState())
        private set

    private val _uiEvent = Channel<ModerationEditUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentTargetId: Int? = null

    fun onEvent(event: ModerationEditEvent) {
        when (event) {
            is ModerationEditEvent.Init -> {
                currentTargetId = event.targetId
                state = state.copy(
                    isNsfw = event.isNsfw,
                    decisionMode = DecisionMode.APPROVE,
                    rejectionReason = "",
                    selectedViolations = emptyList(),
                    isViolationDropdownExpanded = false,
                    error = null,
                    isLoading = false
                )
            }

            is ModerationEditEvent.ChangeDecisionMode -> {
                state = state.copy(decisionMode = event.mode)
            }

            is ModerationEditEvent.ToggleNsfw -> {
                state = state.copy(isNsfw = event.isNsfw)
            }

            is ModerationEditEvent.EnteredRejectionReason -> {
                state = state.copy(rejectionReason = event.value)
            }

            is ModerationEditEvent.ToggleViolation -> {
                val currentList = state.selectedViolations.toMutableList()
                if (currentList.contains(event.violation)) {
                    currentList.remove(event.violation)
                } else {
                    currentList.add(event.violation)
                }
                state = state.copy(selectedViolations = currentList)
            }

            is ModerationEditEvent.ToggleViolationDropdown -> {
                state = state.copy(isViolationDropdownExpanded = !state.isViolationDropdownExpanded)
            }

            is ModerationEditEvent.DismissViolationDropdown -> {
                state = state.copy(isViolationDropdownExpanded = false)
            }

            is ModerationEditEvent.Submit -> {
                submitChanges()
            }
        }
    }

    private fun submitChanges() {
        val targetId = currentTargetId ?: return

        if (state.decisionMode == DecisionMode.APPROVE) {
            handleApproveMode(targetId)
        } else {
            handleRejectMode(targetId)
        }
    }

    private fun handleApproveMode(id: Int) {
        toggleModerationNsfwUseCase(
            id = id,
            isNsfw = state.isNsfw
        ).onEach { result ->
            handleResourceResult(result)
        }.launchIn(viewModelScope)
    }

    private fun handleRejectMode(id: Int) {
        if (state.rejectionReason.isBlank() && state.selectedViolations.isEmpty()) {
            sendUiEvent(
                ModerationEditUiEvent.ShowMessage(
                    UiText.StringResource(
                        R.string.moderation_detail_not_select_error
                    )
                )
            )
            return
        }

        val violationsList =
            state.selectedViolations.ifEmpty { null }

        postModerationMessageUseCase(
            id = id,
            decision = ModerationDecision.REJECT,
            violations = violationsList,
            rejectionReason = state.rejectionReason,
            notes = null,
            isNsfw = null
        ).onEach { result ->
            handleResourceResult(result)
        }.launchIn(viewModelScope)
    }

    private fun <T> handleResourceResult(result: Resource<T>) {
        when (result) {
            is Resource.Loading -> {
                state = state.copy(isLoading = true, error = null)
            }

            is Resource.Success -> {
                state = state.copy(isLoading = false)
                sendUiEvent(ModerationEditUiEvent.Dismiss)
            }

            is Resource.Error -> {
                state = state.copy(isLoading = false, error = result.error.message)
                sendUiEvent(ModerationEditUiEvent.ShowMessage(result.error.message))
            }
        }
    }

    private fun sendUiEvent(event: ModerationEditUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}