package com.itirafapp.android.data.remote.network.interceptor

import com.itirafapp.android.data.remote.auth.dto.RefreshTokenRequest
import com.itirafapp.android.data.remote.network.api.TokenRefreshApi
import com.itirafapp.android.util.manager.SessionEventBus
import com.itirafapp.android.util.manager.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshApi: TokenRefreshApi,
    private val sessionEventBus: SessionEventBus
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        if (response.code == 401) {
            val errorBody = response.peekBody(1024 * 1024).string()

            if (errorBody.contains("1401")) {
                handleSessionExpired()
                return response
            }

            if (errorBody.contains("1402")) {
                response.close()

                synchronized(this) {
                    val currentToken = tokenManager.getAccessToken()
                    val failedRequest = response.request
                    val requestToken = failedRequest.header("Authorization")?.replace("Bearer ", "")

                    if (currentToken != null && requestToken != null && currentToken != requestToken) {
                        val newRequest = failedRequest.newBuilder()
                            .header("Authorization", "Bearer $currentToken")
                            .build()
                        return chain.proceed(newRequest)
                    }

                    val refreshSuccess = refreshTokenSynchronous()

                    if (refreshSuccess) {
                        val newAccessToken = tokenManager.getAccessToken()
                        val newRequest = failedRequest.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                        return chain.proceed(newRequest)
                    } else {
                        handleSessionExpired()
                        return response
                    }
                }
            }
        }
        return response
    }

    private fun refreshTokenSynchronous(): Boolean {
        val refreshToken = tokenManager.getRefreshToken() ?: return false

        return try {
            val call = refreshApi.refreshToken(RefreshTokenRequest(refreshToken))
            val response = call.execute()

            if (response.isSuccessful && response.body() != null) {
                val tokens = response.body()!!
                tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)
                true
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    private fun handleSessionExpired() {
        tokenManager.deleteTokens()
        CoroutineScope(Dispatchers.IO).launch {
            sessionEventBus.triggerSessionExpired()
        }
    }
}