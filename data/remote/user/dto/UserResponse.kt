package com.itirafapp.android.data.remote.user.dto

import com.google.gson.annotations.SerializedName
import com.itirafapp.android.domain.model.Role

data class UserResponse(
    val id: String,
    val email: String,
    val username: String?,
    @SerializedName("anonymous")
    val isAnonymous: Boolean,
    @SerializedName("social_links")
    val socialLinks: List<SocialLinkResponse>?,
    val roles: List<RoleDto>
)

data class SocialLinkResponse(
    val id: String,
    val platform: String,
    val username: String,
    val url: String,
    val verified: Boolean,
    val visible: Boolean
)

data class RoleDto(
    val name: String
)