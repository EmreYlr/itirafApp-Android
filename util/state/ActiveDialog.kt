package com.itirafapp.android.util.state

sealed interface ActiveDialog {
    data class BlockUser(val userId: String, val isReply: Boolean) : ActiveDialog
    data class DeleteItem(val itemId: Int, val isReply: Boolean) : ActiveDialog
}
