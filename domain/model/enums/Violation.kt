package com.itirafapp.android.domain.model.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Violation : Parcelable {
    @SerializedName("NONE")
    NONE,
    @SerializedName("PROFANITY")
    PROFANITY,
    @SerializedName("HARASSMENT")
    HARASSMENT,
    @SerializedName("PERSONAL_INFO")
    PERSONAL_INFO,
    @SerializedName("HATE_SPEECH")
    HATE_SPEECH,
    @SerializedName("THREAT")
    THREAT,
    @SerializedName("SEXUAL_CONTENT")
    SEXUAL_CONTENT,
    @SerializedName("VIOLENCE")
    VIOLENCE,
    @SerializedName("DISCRIMINATION")
    DISCRIMINATION,
    @SerializedName("SPAM")
    SPAM,
    @SerializedName("OTHER")
    OTHER;

    companion object {
        val selectableCases: List<Violation>
            get() = entries.filter { it != NONE }
    }
}