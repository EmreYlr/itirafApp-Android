package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.User
import com.itirafapp.android.util.Resource

interface UserRepository {
    suspend fun getUser(): Resource<User>
    fun getLocalUser(): User?
    fun clearUserData()

}