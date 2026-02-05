package com.itirafapp.android.presentation.screens.message.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.domain.model.SentMessageStatus
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SentMessageDetailHeader(
    item: SentMessage
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
        if (item.confessionTitle.isNotEmpty()) {
            Text(
                text = item.confessionTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ItirafTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = item.confessionMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = ItirafTheme.colors.textSecondary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.confessionAuthorUsername,
                style = MaterialTheme.typography.labelMedium,
                color = ItirafTheme.colors.textTertiary,
                textDecoration = TextDecoration.Underline
            )

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
fun SentMessageDetailHeaderPreview() {
    val mockMessage = SentMessage(
        requestId = "1",
        confessionAuthorUsername = "anonim_yazar",
        initialMessage = "Selam",
        confessionTitle = "Kampüs Kedileri",
        confessionMessage = "Bugün kütüphanenin önündeki sarman kediye mama verdim, çok tatlıydı.",
        channelMessageId = 123,
        createdAt = "2 saat önce",
        status = SentMessageStatus.PENDING
    )

    val mockMessageNoTitle = mockMessage.copy(
        confessionTitle = ""
    )

    ItirafAppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SentMessageDetailHeader(item = mockMessage)

            Spacer(modifier = Modifier.height(20.dp))

            SentMessageDetailHeader(item = mockMessageNoTitle)
        }
    }
}