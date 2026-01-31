package com.itirafapp.android.data.remote.social_link

import com.itirafapp.android.data.remote.social_link.dto.SocialLinkRequest
import com.itirafapp.android.data.remote.social_link.dto.SocialLinkVisibilityRequest
import com.itirafapp.android.data.remote.social_link.dto.UpdateSocialLinkRequest
import com.itirafapp.android.data.remote.social_link.dto.UserSocialLinkResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SocialLinkService {
    @Headers("X-Auth-Restriction: NonAnonymous")
    @POST("social-links")
    suspend fun createSocialLink(
        @Body request: SocialLinkRequest
    )

    @Headers("X-Auth-Restriction: NonAnonymous")
    @GET("social-links")
    suspend fun fetchUserSocialLinks(): UserSocialLinkResponse

    @Headers("X-Auth-Restriction: NonAnonymous")
    @PUT("social-links/{id}")
    suspend fun updateUserSocialLink(
        @Path("id") id: String,
        @Body request: UpdateSocialLinkRequest
    )

    @Headers("X-Auth-Restriction: NonAnonymous")
    @PUT("social-links/{id}/visibility")
    suspend fun updateUserSocialLinkVisibility(
        @Path("id") id: String,
        @Body request: SocialLinkVisibilityRequest
    )

    @Headers("X-Auth-Restriction: NonAnonymous")
    @DELETE("social-links/{id}")
    suspend fun deleteUserSocialLink(
        @Path("id") id: String
    )
}