package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.DirectMessage
import com.itirafapp.android.util.state.Resource

interface RoomRepository {
    suspend fun getAllDirectMessages(): Resource<List<DirectMessage>>
    suspend fun deleteRoom(roomId: String, blockUser: Boolean): Resource<Unit>

    suspend fun requestCreateRoom(
        channelMessageId: Int,
        initialMessage: String,
        shareSocialLinks: Boolean
    ): Resource<Unit>
}