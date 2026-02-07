package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.DirectMessage
import com.itirafapp.android.domain.model.InboxMessage
import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.util.state.Resource

interface RoomRepository {
    suspend fun getAllDirectMessages(): Resource<List<DirectMessage>>
    suspend fun deleteRoom(roomId: String, blockUser: Boolean): Resource<Unit>
    suspend fun getPendingMessages(): Resource<List<InboxMessage>>
    suspend fun approvePendingMessage(requestId: String): Resource<Unit>
    suspend fun rejectPendingMessage(requestId: String): Resource<Unit>
    suspend fun getSentMessages(): Resource<List<SentMessage>>
    suspend fun deleteSentMessageRequest(requestId: String): Resource<Unit>
    suspend fun getRoomMessages(
        roomId: String,
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<MessageData>>
    suspend fun requestCreateRoom(
        channelMessageId: Int,
        initialMessage: String,
        shareSocialLinks: Boolean
    ): Resource<Unit>
}