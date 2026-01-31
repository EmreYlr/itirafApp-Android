package com.itirafapp.android.presentation.screens.profile.social

import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.model.enums.SocialPlatform

data class SocialState(
    val link: Link? = null,
    val username: String = "",
    val fullLink: String = "https://..",
    val platform: SocialPlatform? = null,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val usedPlatforms: List<SocialPlatform> = emptyList(),
    val error: String? = null,
)

sealed class SocialEvent {
    object DeleteIconClicked : SocialEvent()
    object DeleteDialogDismissed : SocialEvent()
    object DeleteConfirmed : SocialEvent()
    object SaveClicked : SocialEvent()
    data class UsernameChanged(val username: String) : SocialEvent()
    data class PlatformChanged(val platform: SocialPlatform) : SocialEvent()
}

sealed class SocialUiEvent {
    object NavigateToBack : SocialUiEvent()
    data class ShowMessage(val message: String) : SocialUiEvent()
}