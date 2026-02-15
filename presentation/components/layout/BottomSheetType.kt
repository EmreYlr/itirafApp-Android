package com.itirafapp.android.presentation.components.layout

import com.itirafapp.android.domain.model.ReportTarget

sealed class BottomSheetType {
    object None : BottomSheetType()
    data class AddPost(val channelId: Int) : BottomSheetType()
    data class DMRequest(val targetId: Int) : BottomSheetType()
    data class Report(val target: ReportTarget) : BottomSheetType()
    data class Moderation(val targetId: Int, val isNsfw: Boolean) : BottomSheetType()
}