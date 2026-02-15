package com.itirafapp.android.presentation.screens.my_confession.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationIcon(
    onClick: () -> Unit,
    isEnabled: Boolean = false
) {
    if (isEnabled) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Filled.AdminPanelSettings,
                contentDescription = "Moderation",
                tint = ItirafTheme.colors.brandPrimary
            )
        }
    }
}