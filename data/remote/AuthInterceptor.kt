package com.itirafapp.android.data.remote

import com.itirafapp.android.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    @param:Named("ClientKey") private val clientKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        requestBuilder.addHeader("Content-Type", "application/json")
        requestBuilder.addHeader("x-client-key", clientKey)

        tokenManager.getAccessToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}