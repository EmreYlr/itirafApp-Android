package com.itirafapp.android.data.remote.social_link.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserSocialLinkResponse(
    val links: List<LinkResponse>
)

@Serializable
data class LinkResponse(
    val id: String,
    val username: String,
    val platform: String,
    val url: String,
    val verified: Boolean = false,
    var visible: Boolean = true,
    val displayOrder: Int? = null,
    val createdAt: String? = null
)