package com.itirafapp.android.data.remote.confession.dto

data class ReplyRequest(
    val message: String
)

data class ShortlinkRequest(
    val messageId: Int
)

data class ReportConfessionRequest(
    val reason: String
)

data class ReportReplyRequest(
    val reason: String
)