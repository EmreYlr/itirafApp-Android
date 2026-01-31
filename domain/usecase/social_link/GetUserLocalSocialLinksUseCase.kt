package com.itirafapp.android.domain.usecase.social_link

import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.repository.SocialLinkRepository
import javax.inject.Inject

class GetUserLocalSocialLinksUseCase @Inject constructor(
    private val repository: SocialLinkRepository
) {
    operator fun invoke(): List<Link> {
        val result = repository.getLocalSocialLinks()

        return result
    }
}