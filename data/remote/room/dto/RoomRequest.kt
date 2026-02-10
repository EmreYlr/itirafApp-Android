package com.itirafapp.android.data.remote.room.dto

data class DMRequest(
    val channelMessageId: Int,
    val initialMessage: String,
    val shareSocialLinks: Boolean
)

data class DeleteRoomRequest(
    val blockUser: Boolean
)

data class ReportRoomRequest(
    val reason: String
)