package com.itirafapp.android.domain.model

data class DirectMessage(
    val roomId: String,
    val username: String,
    val lastMessage: String,
    val lastMessageDate: String,
    val isLastMessageMine: Boolean,
    val unreadMessageCount: Int
)