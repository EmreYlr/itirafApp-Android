package com.itirafapp.android.domain.usecase.notification

import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class DeleteAllNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.deleteAllNotification()
    }
}