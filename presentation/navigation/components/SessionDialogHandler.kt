package com.itirafapp.android.presentation.navigation.components

import androidx.compose.runtime.Composable
import com.itirafapp.android.presentation.components.dialog.LoginRequiredDialog
import com.itirafapp.android.presentation.components.dialog.SessionExpiredDialog
import com.itirafapp.android.util.manager.SessionEvent

@Composable
fun SessionDialogHandler(
    event: SessionEvent?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    when (event) {
        is SessionEvent.LoginRequired -> {
            LoginRequiredDialog(
                onDismiss = onDismiss,
                onConfirm = onConfirm
            )
        }

        is SessionEvent.SessionExpired -> {
            SessionExpiredDialog(
                onConfirm = onConfirm
            )
        }

        null -> {}
    }
}