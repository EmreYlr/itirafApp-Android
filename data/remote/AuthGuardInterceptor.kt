package com.itirafapp.android.data.remote

import com.itirafapp.android.util.SessionEventBus
import com.itirafapp.android.util.UserManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AuthGuardInterceptor @Inject constructor(
    private val userManager: UserManager,
    private val sessionEventBus: SessionEventBus
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requiresNonAnonymous = request.header("X-Auth-Restriction") == "NonAnonymous"

        if (requiresNonAnonymous) {
            val user = userManager.getUser()

            if (user == null || user.anonymous) {

                kotlinx.coroutines.runBlocking {
                    sessionEventBus.triggerLoginRequired()
                }

                throw IOException("Action requires non-anonymous login")
            }
        }

        val newRequest = request.newBuilder()
            .removeHeader("X-Auth-Restriction")
            .build()

        return chain.proceed(newRequest)
    }
}