package com.itirafapp.android.domain.usecase.chat

import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.domain.repository.ChatSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatMessagesUseCase @Inject constructor(
    private val repository: ChatSocketRepository
) {
    operator fun invoke(): Flow<MessageData> {
        return repository.observeMessages()
    }
}