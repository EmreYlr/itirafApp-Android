package com.itirafapp.android.presentation.screens.terms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.auth.LoginAnonymousUseCase
import com.itirafapp.android.domain.usecase.onboarding.CompleteTermsUseCase
import com.itirafapp.android.util.constant.Constants
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val loginAnonymousUseCase: LoginAnonymousUseCase,
    private val termsProvider: TermsProvider,
    private val completeTermsUseCase: CompleteTermsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TermsState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<TermsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadTerms()
    }

    private fun loadTerms() {
        _state.value = _state.value.copy(items = termsProvider.getTerms())
    }

    fun onEvent(event: TermsEvent) {
        when (event) {
            is TermsEvent.OnToggleAcceptance -> {
                _state.value = _state.value.copy(isAccepted = event.isChecked)
            }

            is TermsEvent.OnPrivacyPolicyClick -> {
                sendUiEvent(TermsUiEvent.NavigateToUrl(Constants.TERMS_URL))
            }

            is TermsEvent.OnStartClick -> {
                if (_state.value.isAccepted) {
                    startAnonymousLoginAndComplete()
                }
            }
        }
    }

    private fun startAnonymousLoginAndComplete() {
        viewModelScope.launch {
            loginAnonymousUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }

                    is Resource.Success -> {
                        completeTermsUseCase()

                        _state.value = _state.value.copy(isLoading = false)
                        sendUiEvent(TermsUiEvent.NavigateToHome)
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                        sendUiEvent(TermsUiEvent.NavigateToAuth)
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: TermsUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}