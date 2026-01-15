package com.itirafapp.android.data.repository

import com.itirafapp.android.data.mapper.toDomain
import com.itirafapp.android.data.remote.network.safeApiCall
import com.itirafapp.android.data.remote.user.UserService
import com.itirafapp.android.domain.model.User
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.Resource
import com.itirafapp.android.util.UserManager
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
}