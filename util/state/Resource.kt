package com.itirafapp.android.util.state

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

fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> {
            if (data != null) {
                Resource.Success(transform(data))
            } else {
                Resource.Error("Data is null inside Success")
            }
        }

        is Resource.Error -> {
            Resource.Error(message ?: "Unknown Error", errorCode)
        }

        is Resource.Loading -> {
            Resource.Loading()
        }
    }
}