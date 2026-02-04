package com.itirafapp.android.domain.usecase.notification

import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(
        page: Int,
        limit: Int = 10
    ): Flow<Resource<PaginatedResult<NotificationItem>>> = flow {
        emit(Resource.Loading())
        val result = repository.fetchNotifications(page, limit)
        emit(result)
    }
}