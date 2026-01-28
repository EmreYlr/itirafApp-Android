package com.itirafapp.android.presentation.screens.channel.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun AddPostIconButton(
    isUserAuthenticated: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String? = null
) {
    val iconTint = if (isUserAuthenticated) {
        ItirafTheme.colors.brandPrimary
    } else {
        Color.Gray.copy(alpha = 0.6f)
    }

    IconButton(
        onClick = {
            if (isUserAuthenticated) {
                onClick()
            }
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint
        )
    }
}