package com.itirafapp.android.presentation.screens.report

import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.util.state.UiText

data class ReportState(
    val isLoading: Boolean = false,
    val target: ReportTarget? = null,
    val reason: String = "",
    val error: UiText? = null
)

sealed class ReportEvent {
    data class Init(val target: ReportTarget) : ReportEvent()
    data class ReasonText(val reason: String) : ReportEvent()
    object SubmitClicked : ReportEvent()
}

sealed class ReportUiEvent {
    object Dismiss : ReportUiEvent()
    data class ShowMessage(val message: UiText) : ReportUiEvent()
}