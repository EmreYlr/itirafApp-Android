package com.itirafapp.android.domain.usecase.notification

import com.itirafapp.android.domain.model.NotificationStatus
import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchNotificationCountUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<Resource<NotificationStatus>> = flow  {
        emit(Resource.Loading())

        val result = repository.fetchNotificationStatus()

        emit(result)
    }
}