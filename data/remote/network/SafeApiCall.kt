package com.itirafapp.android.data.remote.network

import com.google.gson.Gson
import com.itirafapp.android.util.APIError
import com.itirafapp.android.util.Resource
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Resource<T> {
    return try {
        val result = apiCall()
        Resource.Success(result)
    } catch (throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                val response = throwable.response()
                val code = throwable.code()
                val errorBody = response?.errorBody()?.string()
                val apiError = try {
                    Gson().fromJson(errorBody, APIError::class.java)
                } catch (_: Exception) {
                    null
                }

                Resource.Error(
                    message = apiError?.message ?: "Bir hata oluştu",
                    errorCode = apiError?.code ?: code
                )
            }

            is IOException -> {
                Resource.Error("İnternet bağlantınızı kontrol edin")
            }

            else -> {
                Resource.Error(throwable.message ?: "Bilinmeyen bir hata oluştu")
            }
        }
    }
}
