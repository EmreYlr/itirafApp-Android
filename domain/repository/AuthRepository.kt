package com.itirafapp.android.domain.repository

import com.itirafapp.android.data.remote.auth.dto.AnonymousLoginRequest
import com.itirafapp.android.data.remote.auth.dto.AnonymousRegisterResponse
import com.itirafapp.android.data.remote.auth.dto.LoginRequest
import com.itirafapp.android.data.remote.auth.dto.RegisterRequest
import com.itirafapp.android.util.Resource

interface AuthRepository {
    suspend fun loginAnonymous(request: AnonymousLoginRequest): Resource<Unit>
    suspend fun registerAnonymous(): Resource<AnonymousRegisterResponse>
    suspend fun loginUser(request: LoginRequest): Resource<Unit>
    suspend fun registerUser(request: RegisterRequest): Resource<Unit>
}