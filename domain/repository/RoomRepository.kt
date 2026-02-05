package com.itirafapp.android.domain.repository

import com.itirafapp.android.util.state.Resource

interface RoomRepository {
    suspend fun requestCreateRoom(
        channelMessageId: Int,
        initialMessage: String,
        shareSocialLinks: Boolean
    ): Resource<Unit>
}