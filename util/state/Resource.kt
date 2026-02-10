package com.itirafapp.android.util.state

import com.itirafapp.android.domain.model.AppError
data class APIError(
    val code: Int,
    val type: String,
    val message: String
)

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: AppError) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Error -> Resource.Error(error)
        is Resource.Loading -> Resource.Loading
    }
}