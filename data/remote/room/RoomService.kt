package com.itirafapp.android.data.remote.room

import com.itirafapp.android.data.remote.room.dto.DMRequest
import com.itirafapp.android.data.remote.room.dto.DeleteRoomRequest
import com.itirafapp.android.data.remote.room.dto.DirectMessageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomService {
    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("rooms/all")
    suspend fun getAllRooms(): List<DirectMessageResponse>

    @Headers("X-Auth-Restriction: NonAnonymous")
    @HTTP(method = "DELETE", path = "rooms/{id}", hasBody = true)
    suspend fun deleteRoom(
        @Path("id") id: String,
        @Body request: DeleteRoomRequest
    ): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("rooms/request")
    suspend fun requestCreateRoom(
        @Body request: DMRequest
    ): Unit
}
