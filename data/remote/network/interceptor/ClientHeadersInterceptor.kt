package com.itirafapp.android.data.remote.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

class ClientHeadersInterceptor @Inject constructor(
    @param:Named("ClientKey") private val clientKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("x-client-key", clientKey)
            .build()
        return chain.proceed(request)
    }
}