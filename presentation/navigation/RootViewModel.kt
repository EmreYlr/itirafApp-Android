package com.itirafapp.android.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.auth.LogoutUserUseCase
import com.itirafapp.android.util.manager.SessionEvent
import com.itirafapp.android.util.manager.SessionEventBus
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val sessionEventBus: SessionEventBus,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {
    val sessionEvents: SharedFlow<SessionEvent> = sessionEventBus.events

    fun onSessionExpiredConfirmed(onComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUserUseCase().collect { result ->
                if (result !is Resource.Loading) {
                    onComplete()
                }
            }
        }
    }
}