package com.itirafapp.android.domain.model

import com.itirafapp.android.domain.model.enums.RoleType

data class User(
    val id: String?,
    val username: String?,
    val email: String,
    val anonymous: Boolean,
    val socialLinks: List<Link>?,
    val roles: List<Role>
)

data class Role(
    val name: RoleType
)