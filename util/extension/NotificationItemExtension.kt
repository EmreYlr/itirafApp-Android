package com.itirafapp.android.util.extension

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.itirafapp.android.domain.model.NotificationEventType

fun NotificationEventType.getBadgeColor(): Color {
    return when (this) {
        NotificationEventType.LIKE -> Color(0xFFF44336)
        NotificationEventType.MESSAGE -> Color(0xFF2196F3)
        NotificationEventType.MESSAGE_REQUEST -> Color(0xFF9C27B0)
        NotificationEventType.MESSAGE_REQUEST_RESULT -> Color(0xFF00BCD4)
        NotificationEventType.COMMENT -> Color(0xFF4CAF50)
        NotificationEventType.MODERATOR -> Color(0xFFFF9800)
        NotificationEventType.CONFESSION -> Color(0xFF3F51B5)
        NotificationEventType.ADMIN_REVIEW_REQUIRED -> Color(0xFFD32F2F)
    }
}

fun NotificationEventType.getIcon(): ImageVector {
    return when (this) {
        NotificationEventType.LIKE -> Icons.Filled.Favorite
        NotificationEventType.MESSAGE -> Icons.Filled.ChatBubble
        NotificationEventType.MESSAGE_REQUEST -> Icons.Filled.PersonAdd
        NotificationEventType.MESSAGE_REQUEST_RESULT -> Icons.Filled.Person
        NotificationEventType.COMMENT -> Icons.Filled.Forum
        NotificationEventType.MODERATOR -> Icons.Filled.Security
        NotificationEventType.CONFESSION -> Icons.Filled.Verified
        NotificationEventType.ADMIN_REVIEW_REQUIRED -> Icons.Filled.Warning
    }
}