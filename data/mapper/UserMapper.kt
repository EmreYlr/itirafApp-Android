package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.user.dto.RoleDto
import com.itirafapp.android.data.remote.user.dto.SocialLinkResponse
import com.itirafapp.android.data.remote.user.dto.UserResponse
import com.itirafapp.android.domain.model.Role
import com.itirafapp.android.domain.model.RoleType
import com.itirafapp.android.domain.model.SocialLink
import com.itirafapp.android.domain.model.User

fun UserResponse.toDomain(): User {
    return User(
        id = id,
        email = email,
        username = username,
        anonymous = isAnonymous,
        socialLinks = socialLinks?.map { it.toDomain() } ?: emptyList(),
        roles = roles.map { it.toDomain() }
    )
}

fun SocialLinkResponse.toDomain(): SocialLink {
    return SocialLink(
        id = id,
        platform = platform,
        username = username,
        url = url,
        verified = verified,
        visible = visible
    )
}

fun RoleDto.toDomain(): Role {
    return Role(
        name = when(this.name) {
            "ADMIN" -> RoleType.ADMIN
            "USER" -> RoleType.USER
            else -> RoleType.USER
        }
    )
}