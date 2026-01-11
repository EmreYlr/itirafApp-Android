package com.itirafapp.android.data.remote

import android.util.Log
import com.itirafapp.android.util.APIError
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkLoggerInterceptor @Inject constructor(
    private val gson: Gson
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val method = request.method
        val path = request.url.encodedPath

        try {
            val response = chain.proceed(request)
            
            if (response.isSuccessful) {
                Log.d("itirafApp_Network", "✅ [SUCCESS] [$method] $path")
            } else {
                val responseBody = response.peekBody(Long.MAX_VALUE)
                val content = responseBody.string()
                
                val apiError = try {
                    gson.fromJson(content, APIError::class.java)
                } catch (_: Exception) {
                    null
                }

                if (apiError != null) {
                    Log.e(
                        "itirafApp_Network",
                        "❌ [API_ERROR] [$method] $path - Code: ${response.code} -> ${apiError.message}"
                    )
                } else {
                    Log.e(
                        "itirafApp_Network",
                        "❌ [HTTP_ERROR] [$method] $path - Code: ${response.code} -> $content"
                    )
                }
            }
            return response
        } catch (e: Exception) {
            Log.e(
                "itirafApp_Network",
                "❌ [NETWORK_FAILURE] [$method] $path - Error: ${e.message}"
            )
            throw e
        }
    }
}
