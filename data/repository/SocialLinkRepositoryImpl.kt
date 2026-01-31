package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.data.remote.social_link.SocialLinkService
import com.itirafapp.android.data.remote.social_link.dto.SocialLinkRequest
import com.itirafapp.android.data.remote.social_link.dto.SocialLinkVisibilityRequest
import com.itirafapp.android.data.remote.social_link.dto.UpdateSocialLinkRequest
import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.model.UserSocialLink
import com.itirafapp.android.domain.repository.SocialLinkRepository
import com.itirafapp.android.util.manager.UserManager
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class SocialLinkRepositoryImpl @Inject constructor(
    private val serviceApi: SocialLinkService,
    private val userManager: UserManager
) : SocialLinkRepository {
    override fun getLocalSocialLinks(): List<Link> {
        return userManager.getUser()?.socialLinks ?: emptyList()
    }

    override suspend fun createSocialLink(
        platform: String,
        username: String
    ): Resource<Unit> {
        return safeApiCall {
            val request = SocialLinkRequest(platform, username)
            serviceApi.createSocialLink(request)
        }.also { result ->
            if (result is Resource.Success) {
                getUserSocialLinks()
            }
        }
    }

    override suspend fun getUserSocialLinks(): Resource<UserSocialLink> {
        return safeApiCall {
            val response = serviceApi.fetchUserSocialLinks()
            val socialLinkDomain = response.toDomain()

            updateLocalUserLinks(socialLinkDomain.links)

            socialLinkDomain
        }
    }

    override suspend fun updateUserSocialLink(
        id: String,
        username: String
    ): Resource<Unit> {
        return safeApiCall {
            val request = UpdateSocialLinkRequest(username)
            serviceApi.updateUserSocialLink(id, request)
        }.also { result ->
            if (result is Resource.Success) {
                val currentUser = userManager.getUser()
                if (currentUser?.socialLinks != null) {
                    val updatedList = currentUser.socialLinks.map { link ->
                        if (link.id == id) link.copy(username = username) else link
                    }
                    updateLocalUserLinks(updatedList)
                }
            }
        }
    }

    override suspend fun updateUserSocialLinkVisibility(
        id: String,
        visible: Boolean
    ): Resource<Unit> {
        return safeApiCall {
            val request = SocialLinkVisibilityRequest(visible)
            serviceApi.updateUserSocialLinkVisibility(id, request)
        }.also { result ->
            if (result is Resource.Success) {
                val currentUser = userManager.getUser()
                if (currentUser?.socialLinks != null) {
                    val updatedList = currentUser.socialLinks.map { link ->
                        if (link.id == id) link.copy(visible = visible) else link
                    }
                    updateLocalUserLinks(updatedList)
                }
            }
        }
    }

    override suspend fun deleteUserSocialLink(id: String): Resource<Unit> {
        return safeApiCall {
            serviceApi.deleteUserSocialLink(id)
        }.also { result ->
            if (result is Resource.Success) {
                val currentUser = userManager.getUser()
                if (currentUser?.socialLinks != null) {
                    val updatedList = currentUser.socialLinks.filter { it.id != id }
                    updateLocalUserLinks(updatedList)
                }
            }
        }
    }

    private fun updateLocalUserLinks(newLinks: List<Link>) {
        val currentUser = userManager.getUser()
        if (currentUser != null) {
            val updatedUser = currentUser.copy(socialLinks = newLinks)
            userManager.saveUser(updatedUser)
        }
    }
}