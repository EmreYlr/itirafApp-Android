package com.itirafapp.android.data.remote.network.interceptor

import com.itirafapp.android.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.header("Authorization") == null) {
            tokenManager.getAccessToken()?.let { token ->
                return chain.proceed(
                    request.newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                )
            }
        }
        return chain.proceed(request)
    }
}