package com.itirafapp.android.presentation.components.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

//TODO: Localizable yapılacak
@Composable
fun SessionExpiredDialog(
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Kapatılamaz */ },
        title = {
            Text(
                text = "Oturumunuz Sona Erdi",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = "Oturum süreniz dolduğundan dolayı yeniden giriş yapmalısınız.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Tamam",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        containerColor = ItirafTheme.colors.backgroundCard,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}