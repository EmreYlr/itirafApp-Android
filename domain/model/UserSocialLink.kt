package com.itirafapp.android.domain.model

import com.itirafapp.android.domain.model.enums.SocialPlatform

data class UserSocialLink(
    val links: List<Link>
)

data class Link(
    val id: String,
    val username: String,
    val platform: SocialPlatform,
    val url: String,
    var visible: Boolean = true,
    val createdAt: String? = null
)
