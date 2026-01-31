package com.itirafapp.android.domain.model

import android.os.Parcelable
import com.itirafapp.android.domain.model.enums.SocialPlatform
import kotlinx.parcelize.Parcelize

data class UserSocialLink(
    val links: List<Link>
)

@Parcelize
data class Link(
    val id: String,
    val username: String,
    val platform: SocialPlatform,
    val url: String,
    var visible: Boolean = true,
    val createdAt: String? = null
) : Parcelable
