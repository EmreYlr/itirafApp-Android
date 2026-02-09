package com.itirafapp.android.presentation.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.itirafapp.android.R

@Composable
fun LoginRequiredDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.must_login_title)) },
        text = { Text(text = stringResource(R.string.must_login_description)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(R.string.login_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
fun LoginRequiredDialogPreview() {
    LoginRequiredDialog(
        onDismiss = {},
        onConfirm = {}
    )
}