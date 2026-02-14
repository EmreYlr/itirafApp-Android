package com.itirafapp.android.data.remote.moderation.dto

import com.google.gson.annotations.SerializedName
import com.itirafapp.android.domain.model.enums.Violation

data class ModerationRequest(
    @SerializedName("messageID")
    val messageID: Int,
    val decision: ModerationDecision,
    val violations: List<Violation>?,
    val rejectionReason: String?,
    val notes: String?,
    val isNsfw: Boolean?
)

enum class ModerationDecision {
    @SerializedName("APPROVE")
    APPROVE,

    @SerializedName("REJECT")
    REJECT
}
