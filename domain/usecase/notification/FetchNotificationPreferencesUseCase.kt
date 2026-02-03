package com.itirafapp.android.domain.usecase.notification

import com.itirafapp.android.domain.model.NotificationPreferences
import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchNotificationPreferencesUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<Resource<NotificationPreferences>> = flow {
        emit(Resource.Loading())

        val result = repository.fetchNotificationPreferences()

        emit(result)
    }
}