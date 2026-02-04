package com.itirafapp.android.presentation.screens.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itirafapp.android.domain.model.NotificationData
import com.itirafapp.android.domain.model.NotificationEventStatus
import com.itirafapp.android.domain.model.NotificationEventType
import com.itirafapp.android.domain.model.NotificationItem
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.getBadgeColor
import com.itirafapp.android.util.extension.getIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationItemRow(
    modifier: Modifier = Modifier,
    item: NotificationItem,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val targetBackgroundColor = when {
        isSelectionMode && isSelected -> ItirafTheme.colors.actionLike.copy(alpha = 0.1f)
        item.seen -> ItirafTheme.colors.backgroundCard.copy(alpha = 0.9f)
        else -> ItirafTheme.colors.brandPrimary.copy(alpha = 0.1f)
    }

    val targetBorderColor = when {
        isSelectionMode && isSelected -> ItirafTheme.colors.actionLike
        item.seen -> Color.Transparent
        else -> ItirafTheme.colors.brandSecondary.copy(alpha = 0.3f)
    }

    val targetBorderWidth = when {
        isSelectionMode && isSelected -> 1.dp
        item.seen -> 0.dp
        else -> 0.5.dp
    }

    val targetBadgeBackgroundColor = if (item.seen) {
        ItirafTheme.colors.backgroundApp.copy(0.5f)
    } else {
        item.eventType.getBadgeColor()
    }

    val backgroundColor by animateColorAsState(targetBackgroundColor, label = "BgAnim")
    val borderColor by animateColorAsState(targetBorderColor, label = "BorderAnim")
    val borderWidth by animateDpAsState(targetBorderWidth, label = "WidthAnim")

    val badgeBgColor by animateColorAsState(targetBadgeBackgroundColor, label = "BadgeBgAnim")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (borderWidth > 0.dp) BorderStroke(borderWidth, borderColor) else null,
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = badgeBgColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.eventType.getIcon(),
                    contentDescription = null,
                    tint = ItirafTheme.colors.pureContrast.copy(alpha = 0.8f),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (item.seen) FontWeight.Normal else FontWeight.SemiBold,
                    color = ItirafTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (item.seen) ItirafTheme.colors.textTertiary else ItirafTheme.colors.textSecondary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.createdAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textTertiary,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun NotificationItemRowPreview() {
    val mockData = NotificationData(
        roomId = null, requestId = null, senderName = "Ahmet",
        senderId = null, messageId = null, commentId = null,
        status = NotificationEventStatus.UNKNOWN, notificationId = null
    )

    val unreadItem = NotificationItem(
        id = "1",
        title = "İtirafın Beğenildi",
        body = "Emre Can Yeler senin 'Yazılım çok zor' başlıklı itirafını beğendi.",
        eventType = NotificationEventType.LIKE,
        seen = false,
        data = mockData,
        createdAt = "2 dk önce"
    )

    val readItem = NotificationItem(
        id = "2",
        title = "Yeni Yorum",
        body = "Katılıyorum, bence de bu konuda haklısın. Devam et!",
        eventType = NotificationEventType.COMMENT,
        seen = true,
        data = mockData,
        createdAt = "1 saat önce"
    )

    val selectedItem = NotificationItem(
        id = "3",
        title = "Mesaj İsteği",
        body = "Merhaba, seninle tanışmak istiyorum. Müsait misin?",
        eventType = NotificationEventType.MESSAGE_REQUEST,
        seen = false,
        data = mockData,
        createdAt = "Dün"
    )
    ItirafAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NotificationItemRow(
                item = unreadItem,
                isSelectionMode = false,
                isSelected = false,
                onClick = {},
                onLongClick = {}
            )

            NotificationItemRow(
                item = readItem,
                isSelectionMode = false,
                isSelected = false,
                onClick = {},
                onLongClick = {}
            )

            NotificationItemRow(
                item = selectedItem,
                isSelectionMode = true,
                isSelected = true,
                onClick = {},
                onLongClick = {}
            )

            NotificationItemRow(
                item = unreadItem.copy(id = "4", title = "Seçilmemiş Öğe"),
                isSelectionMode = true,
                isSelected = false,
                onClick = {},
                onLongClick = {}
            )
        }

    }
}