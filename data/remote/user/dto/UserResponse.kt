package com.itirafapp.android.data.remote.user.dto

import com.google.gson.annotations.SerializedName
import com.itirafapp.android.data.remote.social_link.dto.LinkResponse

data class UserResponse(
    val id: String,
    val email: String,
    val username: String?,
    @SerializedName("anonymous")
    val isAnonymous: Boolean,
    @SerializedName("social_links")
    val socialLinks: List<LinkResponse>?,
    val roles: List<RoleDto>
)

data class RoleDto(
    val name: String
)