package com.itirafapp.android.presentation.screens.message.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.InboxMessage
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun InboxRow(
    item: InboxMessage,
    onRowClick: () -> Unit,
    onApproveClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ItirafTheme.colors.backgroundApp)
            .clickable(onClick = onRowClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(ItirafTheme.colors.backgroundCard),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_chat),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(45.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.message_new_request),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = ItirafTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.requesterUsername,
                style = MaterialTheme.typography.bodyMedium,
                color = ItirafTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = onRejectClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Reject",
                    tint = ItirafTheme.colors.statusError,
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = onApproveClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Approve",
                    tint = ItirafTheme.colors.statusSuccess,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun InboxRowPreview() {
    val mockMessage = InboxMessage(
        requestId = "1",
        roomId = "room1",
        requesterUsername = "emrecanyeler",
        requesterUserId = "user1",
        requesterSocialLinks = emptyList(),
        initialMessage = "Merhaba",
        confessionTitle = "Test Başlık",
        confessionMessage = "Test İçerik",
        channelMessageId = 1,
        createdAt = "10 dk önce"
    )

    val longNameMessage = mockMessage.copy(
        requestId = "2",
        requesterUsername = "cok_uzun_bir_kullanici_adi_test_ediliyor_123"
    )

    ItirafAppTheme {
        Surface(color = ItirafTheme.colors.backgroundApp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InboxRow(
                    item = mockMessage,
                    onRowClick = {},
                    onApproveClick = {},
                    onRejectClick = {}
                )

                InboxRow(
                    item = longNameMessage,
                    onRowClick = {},
                    onApproveClick = {},
                    onRejectClick = {}
                )
            }
        }
    }
}