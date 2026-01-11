package com.itirafapp.android.data.repository

import com.itirafapp.android.data.remote.AuthService
import com.itirafapp.android.data.remote.dto.AnonymousLoginRequest
import com.itirafapp.android.domain.model.User
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.Resource
import com.itirafapp.android.util.TokenManager
import com.itirafapp.android.util.UserManager
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService,
    private val tokenManager: TokenManager,
    private val userManager: UserManager
) : AuthRepository {

    override suspend fun loginAnonymous(): Resource<Unit> {
        return safeApiCall {
            val currentUser = userManager.getUser()
            val email = currentUser?.email ?: run {
                val registerResponse = api.registerAnonymous()

                val newUser = User(
                    email = registerResponse.email,
                    anonymous = true
                )
                userManager.saveUser(newUser)
                newUser.email
            }

            val response = api.loginAnonymous(AnonymousLoginRequest(email))
            tokenManager.saveTokens(response.accessToken, response.refreshToken)

            Unit
        }
    }
}