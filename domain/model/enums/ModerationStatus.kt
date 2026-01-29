package com.itirafapp.android.domain.model.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ModerationStatus : Parcelable {
    @SerializedName("HUMAN_APPROVED")
    HUMAN_APPROVED,
    @SerializedName("AI_APPROVED")
    AI_APPROVED,
    @SerializedName("HUMAN_REJECTED")
    HUMAN_REJECTED,
    @SerializedName("AI_REJECTED")
    AI_REJECTED,
    @SerializedName("PENDING_REVIEW")
    PENDING_REVIEW,
    @SerializedName("NEEDS_HUMAN_REVIEW")
    NEEDS_HUMAN_REVIEW
}