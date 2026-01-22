package com.itirafapp.android.presentation.navigation

import androidx.lifecycle.ViewModel
import com.itirafapp.android.util.manager.SessionEvent
import com.itirafapp.android.util.manager.SessionEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val sessionEventBus: SessionEventBus
) : ViewModel() {

    val sessionEvents: SharedFlow<SessionEvent> = sessionEventBus.events
}