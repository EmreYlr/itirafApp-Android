package com.itirafapp.android.util.extension

import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.enums.ConfessionDisplayStatus
import com.itirafapp.android.domain.model.enums.ModerationStatus

val MyConfessionData.displayStatus: ConfessionDisplayStatus
    get() = when (moderationStatus) {
        ModerationStatus.HUMAN_APPROVED,
        ModerationStatus.AI_APPROVED -> ConfessionDisplayStatus.APPROVED

        ModerationStatus.HUMAN_REJECTED,
        ModerationStatus.AI_REJECTED -> ConfessionDisplayStatus.REJECTED

        ModerationStatus.PENDING_REVIEW,
        ModerationStatus.NEEDS_HUMAN_REVIEW -> ConfessionDisplayStatus.IN_REVIEW
    }