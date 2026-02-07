package com.itirafapp.android.domain.usecase.chat

import com.itirafapp.android.domain.repository.ChatSocketRepository
import javax.inject.Inject

class DisconnectChatUseCase @Inject constructor(
    private val repository: ChatSocketRepository
) {
    operator fun invoke() {
        repository.disconnect()
    }
}