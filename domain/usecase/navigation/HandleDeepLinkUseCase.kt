package com.itirafapp.android.domain.usecase.navigation

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.itirafapp.android.presentation.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class HandleDeepLinkUseCase @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    suspend operator fun invoke(intent: Intent?): String? = withContext(Dispatchers.IO) {
        val data: Uri = intent?.data ?: return@withContext null

        if (data.host != "itirafapp.com" && data.host != "www.itirafapp.com") {
            return@withContext null
        }

        val segments = data.pathSegments

        if (segments.isEmpty()) return@withContext null

        val type = segments[0]

        return@withContext when (type) {
            "confession" if segments.size > 1 -> {
                val id = segments[1]
                Screen.Detail.createRoute(id)
            }

            "s" if segments.size > 1 -> {
                val code = segments[1]
                resolveShortLink(code)
            }

            else -> null
        }
    }

    private fun resolveShortLink(code: String): String? {
        return try {
            val request = Request.Builder()
                .url("https://itirafapp.com/s/$code")
                .head()
                .build()

            val response = okHttpClient.newCall(request).execute()

            val finalUrl = response.request.url.toString()
            response.close()

            val uri = finalUrl.toUri()
            val segments = uri.pathSegments

            if (segments.contains("confession") && segments.size > 1) {
                val id = segments.last()
                Screen.Detail.createRoute(id)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}