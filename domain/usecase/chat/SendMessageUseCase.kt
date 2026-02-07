package com.itirafapp.android.domain.usecase.chat

import com.itirafapp.android.domain.repository.ChatSocketRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatSocketRepository
) {
    operator fun invoke(content: String) {
        if (content.isNotBlank()) {
            repository.sendMessage(content)
        }
    }
}