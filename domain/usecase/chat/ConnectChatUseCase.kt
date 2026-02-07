package com.itirafapp.android.domain.usecase.chat

import com.itirafapp.android.domain.repository.ChatSocketRepository
import javax.inject.Inject

class ConnectChatUseCase @Inject constructor(
    private val repository: ChatSocketRepository
) {
    operator fun invoke(roomId: String) {
        repository.connect(roomId)
    }
}