package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.data.remote.room.RoomService
import com.itirafapp.android.data.remote.room.dto.DMRequest
import com.itirafapp.android.data.remote.room.dto.DeleteRoomRequest
import com.itirafapp.android.data.remote.room.dto.RoomMessagesResponse
import com.itirafapp.android.domain.model.DirectMessage
import com.itirafapp.android.domain.model.InboxMessage
import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.domain.model.PaginatedResult
import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.domain.repository.RoomRepository
import com.itirafapp.android.util.state.Resource
import com.itirafapp.android.util.state.map
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val api: RoomService
) : RoomRepository {
    override suspend fun getAllDirectMessages(): Resource<List<DirectMessage>> {
        return safeApiCall {
            api.getAllRooms().map { it.toDomain() }
        }
    }

    override suspend fun deleteRoom(
        roomId: String,
        blockUser: Boolean
    ): Resource<Unit> {
        return safeApiCall {
            val request = DeleteRoomRequest(
                blockUser = blockUser
            )
            api.deleteRoom(roomId, request)
        }
    }

    override suspend fun getPendingMessages(): Resource<List<InboxMessage>> {
        return safeApiCall {
            api.getPendingMessage().map { it.toDomain() }
        }
    }

    override suspend fun approvePendingMessage(requestId: String): Resource<Unit> {
        return safeApiCall {
            api.approveMessageRequest(requestId)
        }
    }

    override suspend fun rejectPendingMessage(requestId: String): Resource<Unit> {
        return safeApiCall {
            api.rejectMessageRequest(requestId)
        }
    }

    override suspend fun getSentMessages(): Resource<List<SentMessage>> {
        return safeApiCall {
            api.getSentMessage().map { it.toDomain() }
        }
    }

    override suspend fun deleteSentMessageRequest(
        requestId: String
    ): Resource<Unit> {
        return safeApiCall {
            api.deleteSentMessageRequest(requestId)
        }
    }

    override suspend fun getRoomMessages(
        roomId: String,
        page: Int,
        limit: Int
    ): Resource<PaginatedResult<MessageData>> {
        val apiResult: Resource<RoomMessagesResponse> = safeApiCall {
            api.getRoomMessages(roomId, page = page, limit = limit)
        }

        return apiResult.map { response ->
            response.toDomain()
        }
    }

    override suspend fun requestCreateRoom(
        channelMessageId: Int,
        initialMessage: String,
        shareSocialLinks: Boolean
    ): Resource<Unit> {
        return safeApiCall {
            val request = DMRequest(
                channelMessageId = channelMessageId,
                initialMessage = initialMessage,
                shareSocialLinks = shareSocialLinks
            )

            api.requestCreateRoom(request)
        }
    }
}