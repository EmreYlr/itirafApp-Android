package com.itirafapp.android.domain.repository

import com.itirafapp.android.util.Resource

interface AuthRepository {
    suspend fun loginAnonymous(): Resource<Unit>
}