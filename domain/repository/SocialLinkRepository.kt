package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.model.UserSocialLink
import com.itirafapp.android.util.state.Resource

interface SocialLinkRepository {
    fun getLocalSocialLinks(): List<Link>
    suspend fun createSocialLink(
        platform: String,
        username: String
    ): Resource<Unit>

    suspend fun getUserSocialLinks(): Resource<UserSocialLink>

    suspend fun updateUserSocialLink(
        id: String,
        username: String
    ): Resource<Unit>

    suspend fun updateUserSocialLinkVisibility(
        id: String,
        visible: Boolean
    ): Resource<Unit>

    suspend fun deleteUserSocialLink(
        id: String
    ): Resource<Unit>
}
