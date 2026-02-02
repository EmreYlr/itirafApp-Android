package com.itirafapp.android.presentation.screens.profile.settings.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.user.DeleteCurrentUserUseCase
import com.itirafapp.android.domain.usecase.user.GetCurrentUserUseCase
import com.itirafapp.android.util.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val deleteCurrentUserUseCase: DeleteCurrentUserUseCase
) : ViewModel() {

    var state by mutableStateOf(EditProfileState())
        private set

    private val _uiEvent = Channel<EditProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadUserData()
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.DeleteIconClicked -> {
                state = state.copy(showDeleteDialog = true)
            }

            is EditProfileEvent.DeleteDialogDismissed -> {
                state = state.copy(showDeleteDialog = false)

            }

            is EditProfileEvent.DeleteConfirmed -> {
                deleteAccount()
            }
        }
    }

    private fun loadUserData() {
        val currentUser = getCurrentUserUseCase()
        state = state.copy(
            user = currentUser,
            username = currentUser?.username ?: "",
            email = currentUser?.email ?: ""
        )
    }

    private fun deleteAccount() {
        deleteCurrentUserUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }

                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    sendUiEvent(EditProfileUiEvent.ShowMessage("Hesabınız silinmiştir."))
                    sendUiEvent(EditProfileUiEvent.NavigateToLogin)
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message
                    )
                    sendUiEvent(EditProfileUiEvent.ShowMessage(result.message ?: "Hata oluştu"))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: EditProfileUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}