package com.itirafapp.android.data.remote.user

import com.itirafapp.android.data.remote.user.dto.BlockUserRequest
import com.itirafapp.android.data.remote.user.dto.MyConfessionResponse
import com.itirafapp.android.data.remote.user.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @GET("users/me")
    suspend fun getMe(): UserResponse

    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("users/me/blocks")
    suspend fun blockUser(
        @Body request: BlockUserRequest
    ): Unit

    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("users/me/messages")
    suspend fun fetchMyConfessions(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): MyConfessionResponse

    @Headers("X-Auth-Restriction: NonAnonymous")
    @DELETE("users/me")
    suspend fun deleteAccount(): Unit

}