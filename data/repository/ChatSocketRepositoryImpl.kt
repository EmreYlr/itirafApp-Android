package com.itirafapp.android.data.repository

import android.util.Log
import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.domain.repository.ChatSocketRepository
import com.itirafapp.android.util.manager.ChatWebSocketManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import java.time.Instant
import javax.inject.Inject

class ChatSocketRepositoryImpl @Inject constructor(
    private val socketManager: ChatWebSocketManager
) : ChatSocketRepository {

    override fun connect(roomId: String) = socketManager.connect(roomId)
    override fun disconnect() = socketManager.disconnect()
    override fun sendMessage(content: String) = socketManager.sendMessage(content)

    override fun observeConnectionState() = socketManager.connectionState

    override fun observeMessages(): Flow<MessageData> {
        return socketManager.incomingMessages
            .mapNotNull { rawText ->
                Log.d("SocketRepo", "Gelen Mesaj: $rawText")

                if (rawText.isBlank()) return@mapNotNull null

                MessageData(
                    id = System.currentTimeMillis().toInt(),
                    content = rawText,
                    createdAt = getCurrentTime(),
                    isMyMessage = false,
                    isSeen = false,
                    seenAt = null
                )
            }
    }

    private fun getCurrentTime(): String {
        return Instant.now().toString()
    }
}