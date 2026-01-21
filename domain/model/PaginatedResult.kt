package com.itirafapp.android.domain.model

data class PaginatedResult<T>(
    val items: List<T>,
    val page: Int,
    val totalPages: Int,
    val hasNextPage: Boolean
)