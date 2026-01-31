package com.itirafapp.android.data.mapper

import com.itirafapp.android.data.remote.social_link.dto.LinkResponse
import com.itirafapp.android.data.remote.social_link.dto.UserSocialLinkResponse
import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.model.UserSocialLink
import com.itirafapp.android.domain.model.enums.SocialPlatform

fun UserSocialLinkResponse.toDomain(): UserSocialLink {
    val links = this.links.map { it.toDomain() }
    return UserSocialLink(links)
}

fun LinkResponse.toDomain(): Link {
    return Link(
        id = this.id,
        username = this.username,
        platform = SocialPlatform.from(this.platform),
        url = this.url,
        visible = this.visible,
        createdAt = this.createdAt
    )
}