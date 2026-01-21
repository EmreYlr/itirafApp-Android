package com.itirafapp.android.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.model.AuthState
import com.itirafapp.android.domain.usecase.auth.GetAuthStateUseCase
import com.itirafapp.android.domain.usecase.auth.LoginAnonymousUseCase
import com.itirafapp.android.domain.usecase.onboarding.GetOnboardingStatusUseCase
import com.itirafapp.android.presentation.navigation.Screen
import com.itirafapp.android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val loginAnonymousUseCase: LoginAnonymousUseCase,
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase
) : ViewModel() {
    private val _destination = MutableStateFlow<String?>(null)
    val destination = _destination.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        checkStartDestination()
    }

    private fun checkStartDestination() {
        viewModelScope.launch {
            delay(1500)

//            if (!getOnboardingStatusUseCase()) {
//                _destination.value = Screen.Onboarding.route
//                return@launch
//            }

            when (getAuthStateUseCase()) {
                AuthState.AUTHENTICATED, AuthState.ANONYMOUS -> {
                    _destination.value = Screen.MainGraph.route
                }

                AuthState.UNAUTHENTICATED -> {
                    startAnonymousLoginProcess()
                }
            }
        }
    }

    private fun startAnonymousLoginProcess() {
        viewModelScope.launch {
            loginAnonymousUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }

                    is Resource.Success -> {
                        _isLoading.value = false
                        _destination.value = Screen.MainGraph.route
                    }

                    is Resource.Error -> {
                        _isLoading.value = false
                        _destination.value = Screen.AuthGraph.route
                    }
                }
            }
        }
    }
}