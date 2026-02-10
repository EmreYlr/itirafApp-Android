package com.itirafapp.android.domain.usecase.auth

import com.itirafapp.android.data.remote.auth.dto.AnonymousLoginRequest
import com.itirafapp.android.domain.model.User
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.domain.repository.CrashReporter
import com.itirafapp.android.domain.repository.SessionTracker
import com.itirafapp.android.util.manager.UserManager
import com.itirafapp.android.util.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginAnonymousUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userManager: UserManager,
    private val crashReporter: CrashReporter,
    private val sessionTracker: SessionTracker
) {
    operator fun invoke(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)

        val currentUser = userManager.getUser()

        var finalEmail = ""
        var isNewUser = false

        if (currentUser?.email != null) {
            finalEmail = currentUser.email
        } else {
            val registerResult = authRepository.registerAnonymous()

            when (registerResult) {
                is Resource.Error -> {
                    emit(Resource.Error(registerResult.error))
                    return@flow
                }

                is Resource.Success -> {
                    finalEmail = registerResult.data.email
                    isNewUser = true
                }

                is Resource.Loading -> {
                    return@flow
                }
            }
        }

        val anonymousRequest = AnonymousLoginRequest(finalEmail)
        val loginResult = authRepository.loginAnonymous(anonymousRequest)

        if (loginResult is Resource.Error) {
            emit(Resource.Error(loginResult.error))
            return@flow
        }

        crashReporter.setUserId(finalEmail)
        crashReporter.setUserAnonymous(true)
        sessionTracker.setUserId(finalEmail)

        if (isNewUser) {
            val newAnonymousUser = User(
                id = "",
                email = finalEmail,
                username = null,
                anonymous = true,
                socialLinks = emptyList(),
                roles = emptyList()
            )
            userManager.saveUser(newAnonymousUser)
        }

        emit(Resource.Success(Unit))
    }
}