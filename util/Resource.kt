package com.itirafapp.android.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val errorCode: Int? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, errorCode: Int? = null, data: T? = null) : Resource<T>(data, message, errorCode)
    class Loading<T> : Resource<T>()
}

data class APIError(
    val code: Int,
    val type: String,
    val message: String
)