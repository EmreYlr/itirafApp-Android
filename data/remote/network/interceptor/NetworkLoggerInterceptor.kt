package com.itirafapp.android.data.remote.network.interceptor

import android.util.Log
import com.google.gson.Gson
import com.itirafapp.android.domain.repository.CrashReporter
import com.itirafapp.android.util.state.APIError
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkLoggerInterceptor @Inject constructor(
    private val gson: Gson,
    private val crashReporter: CrashReporter
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val method = request.method
        val path = request.url.encodedPath

        crashReporter.setCustomKey("last_api_method", method)
        crashReporter.setCustomKey("last_api_path", path)

        try {
            val response = chain.proceed(request)

            if (response.isSuccessful) {
                Log.d("itirafApp_Network", "✅ [SUCCESS] [$method] $path - ${response.code}")
            } else {
                val responseBody = response.peekBody(Long.MAX_VALUE)
                val content = responseBody.string()
                val code = response.code

                val apiError = try {
                    gson.fromJson(content, APIError::class.java)
                } catch (_: Exception) {
                    null
                }

                val errorMessage = apiError?.message ?: content
                Log.e("itirafApp_Network", "❌ [HTTP_ERROR] $code -> $errorMessage")

                if (code >= 500) {
                    crashReporter.sendNetworkError(
                        throwable = Exception("Server Error: $errorMessage"),
                        endpoint = path,
                        method = method,
                        statusCode = code
                    )
                } else {
                    crashReporter.logMessage("Client Error ($code): $path - $errorMessage")
                }
            }
            return response

        } catch (e: Exception) {
            Log.e("itirafApp_Network", "❌ [FAILURE] ${e.message}")

            crashReporter.sendNetworkError(
                throwable = e,
                endpoint = path,
                method = method,
                statusCode = 0
            )
            throw e
        }
    }
}