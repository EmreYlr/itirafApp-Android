package com.itirafapp.android.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.LoginAnonymousUseCase
import com.itirafapp.android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginAnonymousUseCase: LoginAnonymousUseCase
) : ViewModel() {
    private val _loginResult = MutableStateFlow<Resource<Unit>?>(null)
    val loginResult = _loginResult.asStateFlow()

    init {
        performStartupAction()
    }

    private fun performStartupAction() {
        viewModelScope.launch {
            _loginResult.value = loginAnonymousUseCase()
        }
    }
}