package com.itirafapp.android.presentation.screens.profile.social

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.AppError
import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.usecase.social_link.CreateSocialLinkUseCase
import com.itirafapp.android.domain.usecase.social_link.DeleteSocialLinkUseCase
import com.itirafapp.android.domain.usecase.social_link.GetUserLocalSocialLinksUseCase
import com.itirafapp.android.domain.usecase.social_link.UpdateSocialLinkUseCase
import com.itirafapp.android.util.state.Resource
import com.itirafapp.android.util.state.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
    private val createSocialLinkUseCase: CreateSocialLinkUseCase,
    private val updateSocialLinkUseCase: UpdateSocialLinkUseCase,
    private val deleteSocialLinkUseCase: DeleteSocialLinkUseCase,
    private val getUserLocalSocialLinksUseCase: GetUserLocalSocialLinksUseCase
) : ViewModel() {

    var state by mutableStateOf(SocialState())
        private set

    private val _uiEvent = Channel<SocialUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setInitialData(link: Link?) {
        if (state.link != null || state.username.isNotEmpty()) return

        val existingLinks = getUserLocalSocialLinksUseCase()
        val usedPlatforms = existingLinks.map { it.platform }

        if (link != null) {
            state = state.copy(
                link = link,
                username = link.username,
                platform = link.platform,
                fullLink = link.platform.baseURL + link.username,
                isEditMode = true,
                usedPlatforms = usedPlatforms
            )
        } else {
            state = state.copy(
                link = null,
                username = "",
                platform = null,
                fullLink = "",
                isEditMode = false,
                usedPlatforms = usedPlatforms
            )
        }
    }

    fun onEvent(event: SocialEvent) {
        when (event) {
            is SocialEvent.UsernameChanged -> {
                val baseUrl = state.platform?.baseURL ?: ""
                val fullLink = baseUrl + event.username

                state = state.copy(
                    username = event.username,
                    fullLink = fullLink
                )
            }

            is SocialEvent.PlatformChanged -> {
                val newPlatform = event.platform
                val fullLink = newPlatform.baseURL + state.username

                state = state.copy(
                    platform = newPlatform,
                    fullLink = fullLink
                )
            }

            is SocialEvent.SaveClicked -> {
                saveSocialLink()
            }

            is SocialEvent.DeleteIconClicked -> {
                state = state.copy(showDeleteDialog = true)
            }

            is SocialEvent.DeleteDialogDismissed -> {
                state = state.copy(showDeleteDialog = false)
            }


            is SocialEvent.DeleteConfirmed -> {
                deleteSocialLink()
            }
        }
    }

    private fun saveSocialLink() {
        val username = state.username.trim()
        val platform = state.platform

        if (platform == null) {
            sendUiEvent(
                SocialUiEvent.ShowMessage(
                    UiText.StringResource(R.string.validation_error_select_platform),
                )
            )
            return
        }

        if (username.isBlank()) {
            val usernameError = AppError.ValidationError.EmptyField(
                fieldName = UiText.StringResource(R.string.label_username)
            )
            sendUiEvent(
                SocialUiEvent.ShowMessage(
                    message = usernameError.message
                )
            )
            return
        }

        if (state.isEditMode) {
            val id = state.link?.id ?: return

            updateSocialLinkUseCase(id, username).onEach { result ->
                handleResource(result, "Hesap güncellendi")
            }.launchIn(viewModelScope)

        } else {
            createSocialLinkUseCase(platform.rawValue, username).onEach { result ->
                handleResource(result, "Hesap eklendi")
            }.launchIn(viewModelScope)
        }
    }

    private fun deleteSocialLink() {
        val id = state.link?.id ?: return

        deleteSocialLinkUseCase(id).onEach { result ->
            handleResource(result, "Hesap silindi")
        }.launchIn(viewModelScope)
    }

    private fun handleResource(result: Resource<Unit>, successMessage: String) {
        val successMessage = UiText.DynamicString(successMessage)
        when (result) {
            is Resource.Loading -> {
                state = state.copy(isLoading = true, error = null)
            }

            is Resource.Success -> {
                state = state.copy(isLoading = false)
                sendUiEvent(SocialUiEvent.ShowMessage(successMessage))
                sendUiEvent(SocialUiEvent.NavigateToBack)
            }

            is Resource.Error -> {
                state = state.copy(isLoading = false, error = result.error.message)
                sendUiEvent(SocialUiEvent.ShowMessage(result.error.message))
            }
        }
    }

    private fun sendUiEvent(event: SocialUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }
}