package com.itirafapp.android.domain.model

sealed interface ReportTarget {
    data class Confession(val confessionId: Int) : ReportTarget
    data class Comment(val replyId: Int) : ReportTarget
    data class Room(val roomId: String) : ReportTarget
}