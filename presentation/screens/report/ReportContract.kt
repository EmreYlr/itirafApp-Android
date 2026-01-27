package com.itirafapp.android.presentation.screens.report

import com.itirafapp.android.domain.model.ReportTarget

data class ReportState(
    val isLoading: Boolean = false,
    val target: ReportTarget? = null,
    val reason: String = "",
    val error: String? = null
)

sealed class ReportEvent {
    data class Init(val target: ReportTarget) : ReportEvent()
    data class ReasonText(val reason: String) : ReportEvent()
    object SubmitClicked : ReportEvent()
}

sealed class ReportUiEvent {
    object Dismiss : ReportUiEvent()
    data class ShowMessage(val message: String) : ReportUiEvent()
}