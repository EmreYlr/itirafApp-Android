package com.itirafapp.android.util.manager

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<SessionEvent>()
    val events = _events.asSharedFlow()

    suspend fun triggerLoginRequired() {
        _events.emit(SessionEvent.LoginRequired)
    }

    suspend fun triggerSessionExpired() {
        _events.emit(SessionEvent.SessionExpired)
    }
}

sealed class SessionEvent {
    object LoginRequired : SessionEvent()
    object SessionExpired : SessionEvent()
}