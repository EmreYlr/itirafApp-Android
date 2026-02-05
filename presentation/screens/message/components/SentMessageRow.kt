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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.domain.model.SentMessageStatus
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SentMessageRow(
    item: SentMessage,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ItirafTheme.colors.backgroundCard)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(ItirafTheme.colors.backgroundApp),
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.confessionAuthorUsername,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = ItirafTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Spacer(modifier = Modifier.width(8.dp))

                StatusBadge(status = item.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.you, item.initialMessage),
                style = MaterialTheme.typography.bodyMedium,
                color = ItirafTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun StatusBadge(status: SentMessageStatus) {
    val (backgroundColor, contentColor, text) = when (status) {
        SentMessageStatus.PENDING -> Triple(
            ItirafTheme.colors.statusPending.copy(alpha = 0.2f),
            ItirafTheme.colors.statusPending,
            stringResource(R.string.request_status_pending)
        )

        SentMessageStatus.REJECTED -> Triple(
            ItirafTheme.colors.statusError.copy(alpha = 0.2f),
            ItirafTheme.colors.statusError,
            stringResource(R.string.request_status_rejected)
        )
    }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentMessageRowPreview() {
    val pendingMessage = SentMessage(
        requestId = "1",
        confessionAuthorUsername = "gizemli_kullanici_34",
        initialMessage = "Selam, yazdığın itiraf çok ilgimi çekti.",
        confessionTitle = "Okul Hakkında",
        confessionMessage = "...",
        channelMessageId = 123,
        createdAt = "2024-02-05",
        status = SentMessageStatus.PENDING
    )

    val rejectedMessage = SentMessage(
        requestId = "2",
        confessionAuthorUsername = "ayse_yilmaz",
        initialMessage = "Merhaba, tanışabilir miyiz?",
        confessionTitle = "Kahve",
        confessionMessage = "...",
        channelMessageId = 124,
        createdAt = "2024-02-04",
        status = SentMessageStatus.REJECTED
    )

    ItirafAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SentMessageRow(
                item = pendingMessage,
                onClick = {}
            )

            HorizontalDivider(color = ItirafTheme.colors.dividerColor)

            SentMessageRow(
                item = rejectedMessage,
                onClick = {}
            )
        }
    }

}