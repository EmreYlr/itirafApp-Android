package com.itirafapp.android.data.remote.network.interceptor

import com.itirafapp.android.util.manager.SessionEventBus
import com.itirafapp.android.util.manager.UserManager
import kotlinx.coroutines.runBlocking
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

                runBlocking {
                    sessionEventBus.triggerLoginRequired()
                }

                throw AnonymousAuthException()
            }
        }

        val newRequest = request.newBuilder()
            .removeHeader("X-Auth-Restriction")
            .build()

        return chain.proceed(newRequest)
    }
}

class AnonymousAuthException : IOException("User needs to login")