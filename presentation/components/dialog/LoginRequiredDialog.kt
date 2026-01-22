package com.itirafapp.android.presentation.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LoginRequiredDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Giriş Yapmalısın") },
        text = { Text(text = "Bu işlemi gerçekleştirmek için üye girişi yapman gerekiyor.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Giriş Yap")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Vazgeç")
            }
        }
    )
}