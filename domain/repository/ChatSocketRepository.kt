package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.util.manager.SocketConnectionState
import kotlinx.coroutines.flow.Flow

interface ChatSocketRepository {
    fun connect(roomId: String)
    fun disconnect()
    fun sendMessage(content: String)
    fun observeMessages(): Flow<MessageData>
    fun observeConnectionState(): Flow<SocketConnectionState>
}