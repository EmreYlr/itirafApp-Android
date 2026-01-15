package com.itirafapp.android.domain.model

data class User(
    val id: String?,
    val username: String?,
    val email: String,
    val anonymous: Boolean,
    val socialLinks: List<SocialLink>,
    val roles: List<Role>
)

data class SocialLink(
    val id: String,
    val platform: String,
    val username: String,
    val url: String,
    val verified: Boolean,
    val visible: Boolean
)

data class Role(
    val name: RoleType
)

enum class RoleType {
    ADMIN, USER
}