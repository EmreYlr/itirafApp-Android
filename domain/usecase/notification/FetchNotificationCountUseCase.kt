package com.itirafapp.android.domain.usecase.notification

import com.itirafapp.android.domain.model.NotificationStatus
import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchNotificationCountUseCase @Inject constructor(
    private val repository: NotificationRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<NotificationStatus>> = flow  {
        emit(Resource.Loading)

        if (!userRepository.isUserAuthenticated()) {
            emit(Resource.Success(NotificationStatus(count = 0, hasUnread = false)))
            return@flow
        }

        val result = repository.fetchNotificationStatus()
        emit(result)
    }
}