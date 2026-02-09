package com.itirafapp.android.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.onboarding.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingProvider: OnboardingProvider,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<OnboardingUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadPages()
    }

    private fun loadPages() {
        val pages = onboardingProvider.getOnboardingPages()
        _state.value = _state.value.copy(pages = pages)
    }

    fun onEvent(event: OnboardingEvent) {
        val currentState = _state.value
        val totalPages = currentState.pages.size
        val currentIndex = currentState.currentPageIndex

        when (event) {
            is OnboardingEvent.UpdatePage -> {
                _state.value = currentState.copy(currentPageIndex = event.index)
            }

            is OnboardingEvent.ClickBack -> {
                if (currentIndex > 0) {
                    _state.value = currentState.copy(currentPageIndex = currentIndex - 1)
                }
            }

            is OnboardingEvent.ClickNext -> {
                if (currentIndex < totalPages - 1) {
                    _state.value = currentState.copy(currentPageIndex = currentIndex + 1)
                } else {
                    finishOnboarding()
                }
            }
        }
    }

    private fun finishOnboarding() {
        viewModelScope.launch {
            completeOnboardingUseCase()
            _uiEvent.send(OnboardingUiEvent.NavigateToTerms)
        }
    }
}