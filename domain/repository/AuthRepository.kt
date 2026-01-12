package com.itirafapp.android.domain.repository

import com.itirafapp.android.data.remote.dto.LoginRequest
import com.itirafapp.android.data.remote.dto.RegisterRequest
import com.itirafapp.android.util.Resource

interface AuthRepository {
    suspend fun loginAnonymous(): Resource<Unit>
    suspend fun loginUser(request: LoginRequest): Resource<Unit>
    suspend fun registerUser(request: RegisterRequest): Resource<Unit>
}