package com.itirafapp.android.presentation.screens.profile.settings.edit_profile

import com.itirafapp.android.domain.model.User
import com.itirafapp.android.util.state.UiText

data class EditProfileState(
    val user: User? = null,
    val username: String = "",
    val email: String = "",
    val showDeleteDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null,
)

sealed class EditProfileEvent {
    object DeleteIconClicked : EditProfileEvent()
    object DeleteDialogDismissed : EditProfileEvent()
    object DeleteConfirmed : EditProfileEvent()
}

sealed class EditProfileUiEvent {
    object NavigateToLogin : EditProfileUiEvent()
    data class ShowMessage(val message: UiText) : EditProfileUiEvent()
}