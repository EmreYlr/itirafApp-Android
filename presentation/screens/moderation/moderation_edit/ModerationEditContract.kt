package com.itirafapp.android.presentation.screens.moderation.moderation_edit

import com.itirafapp.android.domain.model.enums.Violation
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.DecisionMode
import com.itirafapp.android.util.state.UiText

data class ModerationEditState(
    val isLoading: Boolean = false,
    val decisionMode: DecisionMode = DecisionMode.APPROVE,
    val isNsfw: Boolean = false,
    val rejectionReason: String = "",
    val isViolationDropdownExpanded: Boolean = false,
    val selectedViolations: List<Violation> = emptyList(),
    val availableViolations: List<Violation> = Violation.selectableCases,
    val error: UiText? = null,
)

sealed class ModerationEditEvent {
    data class Init(val targetId: Int, val isNsfw: Boolean) : ModerationEditEvent()
    data class ChangeDecisionMode(val mode: DecisionMode) : ModerationEditEvent()
    data class ToggleNsfw(val isNsfw: Boolean) : ModerationEditEvent()
    data class EnteredRejectionReason(val value: String) : ModerationEditEvent()
    data class ToggleViolation(val violation: Violation) : ModerationEditEvent()
    object DismissViolationDropdown : ModerationEditEvent()
    object ToggleViolationDropdown : ModerationEditEvent()
    object Submit : ModerationEditEvent()
}

sealed class ModerationEditUiEvent {
    object Dismiss : ModerationEditUiEvent()
    data class ShowMessage(val message: UiText) : ModerationEditUiEvent()
}