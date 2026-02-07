package com.itirafapp.android.data.remote.room

import com.itirafapp.android.data.remote.room.dto.DMRequest
import com.itirafapp.android.data.remote.room.dto.DeleteRoomRequest
import com.itirafapp.android.data.remote.room.dto.DirectMessageResponse
import com.itirafapp.android.data.remote.room.dto.InboxMessageResponse
import com.itirafapp.android.data.remote.room.dto.RoomMessagesResponse
import com.itirafapp.android.data.remote.room.dto.SentMessageResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    @GET("rooms/requests/pending")
    suspend fun getPendingMessage(): List<InboxMessageResponse>

    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("rooms/requests/{id}/approve")
    suspend fun approveMessageRequest(
        @Path("id") id: String
    ): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("rooms/requests/{id}/reject")
    suspend fun rejectMessageRequest(
        @Path("id") id: String
    ): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("rooms/requests/sent")
    suspend fun getSentMessage(): List<SentMessageResponse>

    @Headers("X-Auth-Restriction: NonAnonymous")
    @DELETE("rooms/requests/{id}")
    suspend fun deleteSentMessageRequest(
        @Path("id") id: String
    ): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("rooms/{id}/messages")
    suspend fun getRoomMessages(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): RoomMessagesResponse


    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("rooms/request")
    suspend fun requestCreateRoom(
        @Body request: DMRequest
    ): Unit
}
