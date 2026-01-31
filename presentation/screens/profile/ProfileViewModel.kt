package com.itirafapp.android.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.domain.usecase.social_link.GetUserSocialLinksUseCase
import com.itirafapp.android.domain.usecase.social_link.UpdateSocialLinkVisibilityUseCase
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
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserSocialLinksUseCase: GetUserSocialLinksUseCase,
    private val updateSocialLinkVisibilityUseCase: UpdateSocialLinkVisibilityUseCase
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        initializeProfile()
    }

    private fun initializeProfile() {
        val localUser = getCurrentUserUseCase()
        state = state.copy(user = localUser)

        if (localUser?.socialLinks.isNullOrEmpty()) {
            fetchLatestSocialLinks()
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.SettingsClicked -> {
                sendUiEvent(ProfileUiEvent.NavigateToSettings)
            }

            is ProfileEvent.AddSocialClick -> {
                sendUiEvent(ProfileUiEvent.NavigateToSocial(null))
            }

            is ProfileEvent.EditSocialClick -> {
                val link = state.user?.socialLinks?.find { it.id == event.id }
                sendUiEvent(ProfileUiEvent.NavigateToSocial(link))
            }

            is ProfileEvent.FollowChannelClicked -> {
                sendUiEvent(ProfileUiEvent.NavigateToFollowChannel)
            }

            is ProfileEvent.SocialVisibilityChanged -> {
                updateVisibility(event.id, event.isVisibility)
            }
        }
    }

    private fun fetchLatestSocialLinks() {
        getUserSocialLinksUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    val newLinks = result.data?.links
                    val updatedUser = state.user?.copy(socialLinks = newLinks)

                    state = state.copy(
                        isLoading = false,
                        user = updatedUser
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateVisibility(id: String, isVisible: Boolean) {
        val currentUser = state.user ?: return
        val currentLinks = currentUser.socialLinks ?: return

        val updatedLinks = currentLinks.map { link ->
            if (link.id == id) link.copy(visible = isVisible) else link
        }
        state = state.copy(user = currentUser.copy(socialLinks = updatedLinks))

        updateSocialLinkVisibilityUseCase(id, isVisible).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    val revertedLinks = currentLinks.map { link ->
                        if (link.id == id) link.copy(visible = !isVisible) else link
                    }
                    state = state.copy(user = currentUser.copy(socialLinks = revertedLinks))

                    sendUiEvent(
                        ProfileUiEvent.ShowMessage(
                            result.message ?: "Güncelleme başarısız"
                        )
                    )
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    private fun sendUiEvent(event: ProfileUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}