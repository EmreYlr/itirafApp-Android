package com.itirafapp.android.presentation.screens.my_confession.my_confession_edit

import com.itirafapp.android.domain.model.MyConfessionData

data class MyConfessionEditState(
    val isLoading: Boolean = false,
    val title: String? = "",
    val message: String = "",
    val confessions: MyConfessionData? = null,
    val isRefreshing: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val error: String? = null,
)

sealed class MyConfessionEditEvent {
    object BackClicked : MyConfessionEditEvent()
    object DeleteIconClicked : MyConfessionEditEvent()
    object DeleteDialogDismissed : MyConfessionEditEvent()
    object DeleteConfirmed : MyConfessionEditEvent()
    data class TitleTextChanged(val text: String) : MyConfessionEditEvent()
    data class MessageTextChanged(val text: String) : MyConfessionEditEvent()
    object SendClicked : MyConfessionEditEvent()
}

sealed class MyConfessionEditUiEvent {
    object NavigateToBack : MyConfessionEditUiEvent()
    object NavigateToRoot : MyConfessionEditUiEvent()
    data class ShowMessage(val message: String) : MyConfessionEditUiEvent()
}