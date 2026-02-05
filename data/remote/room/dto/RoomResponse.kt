package com.itirafapp.android.data.remote.room.dto

data class DirectMessageResponse(
    val roomId: String,
    val username: String,
    val lastMessage: String,
    val lastMessageDate: String,
    val isLastMessageMine: Boolean,
    val status: String,
    val unreadMessageCount: Int,
)
