package com.itirafapp.android.data.repository

import com.itirafapp.android.data.remote.AuthService
import com.itirafapp.android.data.remote.UserService
import com.itirafapp.android.data.remote.dto.AnonymousLoginRequest
import com.itirafapp.android.data.remote.dto.LoginRequest
import com.itirafapp.android.data.remote.dto.RegisterRequest
import com.itirafapp.android.domain.model.User
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.util.Resource
import com.itirafapp.android.util.TokenManager
import com.itirafapp.android.util.UserManager
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService,
    private val userApi: UserService,
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
                    anonymous = true,
                    id = "",
                    username = null,
                    socialLinkResponse = null,
                    roles = emptyList()
                )

                userManager.saveUser(newUser)
                newUser.email
            }

            val response = api.loginAnonymous(AnonymousLoginRequest(email))
            tokenManager.saveTokens(response.accessToken, response.refreshToken)
        }
    }

    override suspend fun loginUser(request: LoginRequest): Resource<Unit> {
        return safeApiCall {
            val response = api.loginUser(request)
            tokenManager.saveTokens(response.accessToken, response.refreshToken)

            val userProfile = userApi.getMe()
            val user = User(
                id = userProfile.id,
                email = userProfile.email,
                username = userProfile.username,
                anonymous = userProfile.isAnonymous,
                socialLinkResponse = userProfile.socialLinks,
                roles = userProfile.roles
            )
            userManager.deleteUser()
            userManager.saveUser(user)
        }
    }

    override suspend fun registerUser(request: RegisterRequest): Resource<Unit> {
        return safeApiCall {
            api.registerUser(request)
        }
    }
}