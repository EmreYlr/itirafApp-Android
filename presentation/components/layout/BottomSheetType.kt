package com.itirafapp.android.presentation.components.layout

import com.itirafapp.android.domain.model.ReportTarget

sealed class BottomSheetType {
    object None : BottomSheetType()
    data class DMRequest(val targetId: Int) : BottomSheetType()
    data class Report(val target: ReportTarget) : BottomSheetType()
}