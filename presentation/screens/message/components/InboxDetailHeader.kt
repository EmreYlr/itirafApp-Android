package com.itirafapp.android.presentation.screens.message.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.InboxMessage
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun InboxDetailHeader(
    item: InboxMessage
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = ItirafTheme.colors.dividerColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(ItirafTheme.colors.backgroundCard)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.inbox_detail_header),
                style = MaterialTheme.typography.labelMedium,
                color = ItirafTheme.colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (item.confessionTitle.isNotEmpty()) {
            Text(
                text = item.confessionTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ItirafTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            text = item.confessionMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = ItirafTheme.colors.textSecondary,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.createdAt,
                style = MaterialTheme.typography.labelSmall,
                color = ItirafTheme.colors.textTertiary,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun InboxDetailHeaderPreview() {
    val mockMessage = InboxMessage(
        requestId = "1",
        roomId = "room1",
        requesterUsername = "talip_kisi",
        requesterUserId = "user1",
        requesterSocialLinks = emptyList(),
        initialMessage = "Selam",
        confessionTitle = "Kütüphane Hakkında",
        confessionMessage = "Bugün kütüphanede gördüğüm kırmızı kazaklı çocuk çok tatlıydı.",
        channelMessageId = 123,
        createdAt = "2 saat önce"
    )

    ItirafAppTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            InboxDetailHeader(item = mockMessage)
        }
    }
}