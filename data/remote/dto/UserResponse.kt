package com.itirafapp.android.data.remote.dto

import androidx.compose.ui.text.toUpperCase
import com.google.gson.annotations.SerializedName
import com.itirafapp.android.domain.model.Role
import com.itirafapp.android.domain.model.User
import java.util.Locale

data class UserResponse(
    val id: String,
    val email: String,
    val username: String?,
    @SerializedName("anonymous") val isAnonymous: Boolean,
    @SerializedName("social_links") val socialLinks: List<SocialLinkResponse>?,
    val roles: List<Role>
)

data class SocialLinkResponse(
    val id: String,
    val platform: String,
    val username: String,
    val url: String,
    val verified: Boolean,
    val visible: Boolean
)