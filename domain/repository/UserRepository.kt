package com.itirafapp.android.domain.repository

import com.itirafapp.android.domain.model.User
import com.itirafapp.android.util.state.Resource

interface UserRepository {
    suspend fun getUser(): Resource<User>
    suspend fun blockUser(targetUserId: String): Resource<Unit>
    fun getLocalUser(): User?
    fun clearUserData()
    fun isUserAuthenticated(): Boolean
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(completed: Boolean)

}