package com.itirafapp.android.domain.usecase.social_link

import com.itirafapp.android.domain.model.UserSocialLink
import com.itirafapp.android.domain.repository.SocialLinkRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserSocialLinksUseCase @Inject constructor(
    private val repository: SocialLinkRepository
) {
    operator fun invoke(): Flow<Resource<UserSocialLink>> = flow {
        emit(Resource.Loading())
        val result = repository.getUserSocialLinks()
        emit(result)
    }
}