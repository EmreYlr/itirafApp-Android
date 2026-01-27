package com.itirafapp.android.data.remote.room

import com.itirafapp.android.data.remote.room.dto.BlockRoomRequest
import com.itirafapp.android.data.remote.room.dto.DMRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RoomService {
    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("rooms/request")
    suspend fun requestCreateRoom(
        @Body request: DMRequest
    ): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("rooms/blocks")
    suspend fun blockRoom(
        @Body request: BlockRoomRequest
    ): Unit

}
