package com.itirafapp.android.data.remote.social_link.dto

data class SocialLinkRequest(
    val platform: String,
    val username: String
)

data class UpdateSocialLinkRequest(
    val username: String
)

data class SocialLinkVisibilityRequest(
    val visible: Boolean
)