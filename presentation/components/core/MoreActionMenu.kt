package com.itirafapp.android.presentation.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun MoreActionMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    isMine: Boolean,
    onDelete: () -> Unit,
    onReport: () -> Unit,
    onBlock: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(ItirafTheme.colors.backgroundCard)
    ) {
        if (isMine) {
            DropdownMenuItem(
                text = { Text("Sil", color = ItirafTheme.colors.statusError) },
                onClick = {
                    onDelete()
                    onDismiss()
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = ItirafTheme.colors.statusError
                    )
                }
            )
        } else {
            DropdownMenuItem(
                text = { Text("Kullanıcıyı Engelle") },
                onClick = {
                    onBlock()
                    onDismiss()
                },
                leadingIcon = {
                    Icon(Icons.Default.Block, contentDescription = null)
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = ItirafTheme.colors.dividerColor
            )

            DropdownMenuItem(
                text = { Text("Şikayet Et", color = ItirafTheme.colors.statusError) },
                onClick = {
                    onReport()
                    onDismiss()
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Report, contentDescription = null,
                        tint = ItirafTheme.colors.statusError
                    )
                }
            )
        }
    }
}