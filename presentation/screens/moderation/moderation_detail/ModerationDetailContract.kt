package com.itirafapp.android.presentation.screens.moderation.moderation_detail

import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.enums.Violation
import com.itirafapp.android.util.state.UiText

data class ModerationDetailState(
    val isLoading: Boolean = false,
    val moderationData: ModerationData? = null,
    val error: UiText? = null,
    val decisionMode: DecisionMode = DecisionMode.APPROVE,
    val note: String = "",
    val isNsfw: Boolean = false,
    val rejectionReason: String = "",
    val isViolationDropdownExpanded: Boolean = false,
    val selectedViolations: List<Violation> = emptyList(),
    val availableViolations: List<Violation> = Violation.selectableCases
)

sealed class ModerationDetailEvent {
    data class ChangeDecisionMode(val mode: DecisionMode) : ModerationDetailEvent()
    data class EnteredNote(val value: String) : ModerationDetailEvent()

    data class ToggleNsfw(val isNsfw: Boolean) : ModerationDetailEvent()

    data class EnteredRejectionReason(val value: String) : ModerationDetailEvent()
    data class ToggleViolation(val violation: Violation) : ModerationDetailEvent()
    object DismissViolationDropdown : ModerationDetailEvent()
    object ToggleViolationDropdown : ModerationDetailEvent()

    object Submit : ModerationDetailEvent()
}

sealed class ModerationDetailUiEvent {
    data class ShowMessage(val message: UiText) : ModerationDetailUiEvent()
    object NavigateBack : ModerationDetailUiEvent()
}

enum class DecisionMode {
    APPROVE,
    REJECT
}