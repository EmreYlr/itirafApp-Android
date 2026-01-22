package com.itirafapp.android.presentation.model

import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.util.state.UiText

data class ConfessionUiModel(
    val id: Int,
    val title: String,
    val message: String,
    val liked: Boolean,
    val likeCount: Int,
    val replyCount: Int,
    val createdAt: String,
    val owner: OwnerUiModel,
    val channel: ChannelData,
    val isNsfw: Boolean,
    val isMine: Boolean = false
)

data class OwnerUiModel(
    val id: String,
    val username: UiText
)

fun ConfessionUiModel.toggleLikeState(): ConfessionUiModel {
    return copy(
        liked = !liked,
        likeCount = if (liked) likeCount - 1 else likeCount + 1
    )
}