package com.itirafapp.android.presentation.screens.home.components

import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationIcon(
    hasUnread: Boolean,
    notificationCount: Int,
    onClick: () -> Unit,
    isEnabled: Boolean
) {
    IconButton(onClick = onClick, enabled = isEnabled) {
        BadgedBox(
            modifier = Modifier.offset(x = (-4).dp),
            badge = {
                if (hasUnread) {
                    Badge(
                        containerColor = ItirafTheme.colors.brandSecondary,
                        contentColor = Color.White
                    ) {
                        if (notificationCount > 0) {
                            val badgeText = if (notificationCount > 9) "9+" else notificationCount.toString()
                            Text(text = badgeText)
                        }
                    }
                }
            }
        ) {
            Icon(
                imageVector = if (hasUnread) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                contentDescription = "notifications",
                tint = if (isEnabled) ItirafTheme.colors.brandPrimary else ItirafTheme.colors.dividerColor,
            )
        }
    }
}