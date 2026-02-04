package com.itirafapp.android.domain.usecase.notification

import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class MarkNotificationSeenUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationIds: List<String>): Resource<Unit> {
        return repository.markNotificationAsSeen(notificationIds)
    }
}