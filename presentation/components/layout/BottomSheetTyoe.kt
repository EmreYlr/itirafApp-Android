package com.itirafapp.android.presentation.components.layout

sealed class BottomSheetType {
    object None : BottomSheetType()
    data class DMRequest(val targetId: Int) : BottomSheetType()
    // data class Report(val contentId: Int) : BottomSheetType()
}