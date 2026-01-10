package com.itirafapp.android.data.repository

import android.util.Log
import com.google.gson.Gson
import com.itirafapp.android.util.APIError
import com.itirafapp.android.util.Resource
import retrofit2.HttpException
import java.io.IOException

abstract class BaseRepository {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return try {
            val result = apiCall()
            Log.d("itirafApp_Network", "✅ [SUCCESS] - İstek başarıyla tamamlandı.")

            Resource.Success(result)
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val response = throwable.response()
                    val request = response?.raw()?.request

                    val method = request?.method ?: "UNKNOWN"
                    val path = request?.url?.encodedPath ?: ""
                    val code = throwable.code()

                    Log.e("itirafApp_Network", "❌ [$method] $path - Status: $code")

                    val errorBody = response?.errorBody()?.string()
                    val apiError = try {
                        Gson().fromJson(errorBody, APIError::class.java)
                    } catch (_: Exception) {
                        null
                    }

                    if (apiError != null) {
                        Log.e(
                            "itirafApp_Network",
                            "[${apiError.code}] -> Sunucu Hatası: ${apiError.message}"
                        )
                    } else {
                        Log.e("itirafApp_Network", "-> Ağ Hatası: ${throwable.message()}")
                    }

                    Resource.Error(
                        message = apiError?.message ?: "Bir hata oluştu",
                        errorCode = apiError?.code ?: code
                    )
                }

                is IOException -> {
                    Log.e(
                        "itirafApp_Network",
                        "❌ [Network] -> Bağlantı Hatası: ${throwable.message}"
                    )
                    Resource.Error("İnternet bağlantınızı kontrol edin")
                }

                else -> {
                    Log.e(
                        "itirafApp_Network",
                        "❌ [Unknown] -> Beklenmedik Hata: ${throwable.message}"
                    )
                    Resource.Error(throwable.message ?: "Bilinmeyen bir hata oluştu")
                }
            }
        }
    }
}