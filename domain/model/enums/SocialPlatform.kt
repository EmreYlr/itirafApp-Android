package com.itirafapp.android.domain.model.enums

import com.itirafapp.android.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SocialPlatform(val rawValue: String) {
    @SerialName("twitter")
    TWITTER("twitter"),

    @SerialName("instagram")
    INSTAGRAM("instagram"),

    @SerialName("other")
    OTHER("other");

    val displayName: String
        get() = when (this) {
            TWITTER -> "X (Twitter)"
            INSTAGRAM -> "Instagram"
            OTHER -> "Other"
        }

    val iconResId: Int
        get() = when (this) {
            TWITTER -> R.drawable.icon_x
            INSTAGRAM -> R.drawable.icon_instagram
            OTHER -> R.drawable.icon_x
        }

    val baseURL: String
        get() = when (this) {
            TWITTER -> "https://twitter.com/"
            INSTAGRAM -> "https://www.instagram.com/"
            OTHER -> ""
        }

    companion object {
        fun from(value: String?): SocialPlatform {
            return entries.find { it.rawValue.equals(value, ignoreCase = true) } ?: OTHER
        }
    }
}