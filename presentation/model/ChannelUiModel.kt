package com.itirafapp.android.presentation.model

data class ChannelUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val imageURL: String?,
    val isFollowing: Boolean = false
)