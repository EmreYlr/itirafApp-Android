package com.itirafapp.android.domain.usecase.notification

import com.itirafapp.android.domain.model.NotificationPreferencesUpdate
import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateNotificationPreferencesUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(updateModel: NotificationPreferencesUpdate): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        val result = repository.updateNotificationPreferences(updateModel)
        emit(result)
    }
}