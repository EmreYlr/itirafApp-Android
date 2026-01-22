package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.data.remote.user.UserService
import com.itirafapp.android.domain.model.User
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.manager.UserManager
import com.itirafapp.android.util.state.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserService,
    private val userManager: UserManager
) : UserRepository {

    override suspend fun getUser(): Resource<User> {
        return safeApiCall {
            val response = userApi.getMe()

            val user = response.toDomain()

            userManager.deleteUser()
            userManager.saveUser(user)

            user
        }
    }

    override fun getLocalUser(): User? {
        return userManager.getUser()
    }

    override fun clearUserData() {
        userManager.deleteUser()
    }

    override fun isUserAuthenticated(): Boolean {
        val user = getLocalUser()
        return user != null && !user.anonymous
    }

    override fun isOnboardingCompleted(): Boolean {
        return userManager.isOnboardingCompleted()
    }

    override fun setOnboardingCompleted(completed: Boolean) {
        userManager.setOnboardingCompleted(completed)
    }
}