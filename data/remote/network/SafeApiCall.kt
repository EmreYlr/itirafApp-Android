package com.itirafapp.android.data.remote.network

import com.google.gson.Gson
import com.itirafapp.android.data.remote.network.interceptor.AnonymousAuthException
import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.util.state.APIError
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            val result = apiCall()
            Resource.Success(result)
        } catch (throwable: Throwable) {

            val appError: AppError = when (throwable) {
                is AnonymousAuthException -> {
                    AppError.AuthError.AnonymousUser
                }

                is HttpException -> {
                    val code = throwable.code()
                    val errorBodyString = throwable.response()?.errorBody()?.string()

                    val apiErrorDto = try {
                        if (errorBodyString != null) {
                            Gson().fromJson(errorBodyString, APIError::class.java)
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null
                    }

                    AppError.ApiError(
                        code = apiErrorDto?.code ?: code,
                        type = apiErrorDto?.type ?: "UNKNOWN",
                        serverDebugMessage = apiErrorDto?.message,
                        userMessage = null,
                        userTitle = null
                    )
                }

                is IOException -> {
                    AppError.LocalError.NoInternet
                }

                else -> {
                    AppError.LocalError.Unknown
                }
            }

            Resource.Error(appError)
        }
    }
}
