package com.itirafapp.android.presentation.screens.profile

import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.model.User
import com.itirafapp.android.util.state.UiText

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isAnonymous: Boolean = false,
    val error: UiText? = null,
)

sealed class ProfileEvent {
    object Refresh : ProfileEvent()
    object SettingsClicked : ProfileEvent()
    object FollowChannelClicked : ProfileEvent()
    object AddSocialClick : ProfileEvent()
    data class EditSocialClick(val id: String) : ProfileEvent()
    data class SocialVisibilityChanged(val id: String, val isVisibility: Boolean) : ProfileEvent()
}

sealed class ProfileUiEvent {
    object NavigateToSettings : ProfileUiEvent()
    object NavigateToFollowChannel : ProfileUiEvent()
    data class NavigateToSocial(val link: Link?) : ProfileUiEvent()
    data class ShowMessage(val message: UiText) : ProfileUiEvent()
}