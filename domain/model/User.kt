package com.itirafapp.android.domain.model

import com.google.gson.annotations.SerializedName
import com.itirafapp.android.data.remote.dto.SocialLinkResponse

data class User(
    val id: String? = null,
    val username: String? = null,
    val email: String,
    val anonymous: Boolean,
    val socialLinkResponse: List<SocialLinkResponse>?,
    val roles: List<Role>
)

enum class RoleType {
    @SerializedName("ADMIN")
    ADMIN,

    @SerializedName("USER")
    USER
}

data class Role(
    val name: RoleType
)