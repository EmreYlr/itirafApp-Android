package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.user.dto.RoleDto
import com.itirafapp.android.data.remote.user.dto.UserBlockResponse
import com.itirafapp.android.data.remote.user.dto.UserResponse
import com.itirafapp.android.domain.model.BlockedUsers
import com.itirafapp.android.domain.model.Role
import com.itirafapp.android.domain.model.User
import com.itirafapp.android.domain.model.enums.RoleType
import com.itirafapp.android.util.extension.formatToDateTime

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

fun RoleDto.toDomain(): Role {
    return Role(
        name = when(this.name) {
            "ADMIN" -> RoleType.ADMIN
            "USER" -> RoleType.USER
            else -> RoleType.USER
        }
    )
}

fun UserBlockResponse.toDomain(): BlockedUsers {
    return BlockedUsers(
        userId = userId,
        username = username,
        blockedAt = formatToDateTime(blockedAt)
    )
}