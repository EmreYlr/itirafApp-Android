package com.itirafapp.android.presentation.screens.message.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.domain.model.SentMessageStatus
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SentMessageDetailBody(
    message: SentMessage
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomEnd = 2.dp,
                            bottomStart = 12.dp
                        )
                    )
                    .background(ItirafTheme.colors.brandSecondary)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = message.initialMessage,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ItirafTheme.colors.backgroundCard),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_chat),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            val (statusIcon, statusText, statusColor) = when (message.status) {
                SentMessageStatus.PENDING -> Triple(
                    Icons.Rounded.AccessTime,
                    stringResource(R.string.request_status_pending),
                    ItirafTheme.colors.textSecondary
                )

                SentMessageStatus.REJECTED -> Triple(
                    Icons.Rounded.Close,
                    stringResource(R.string.request_status_rejected),
                    ItirafTheme.colors.textSecondary
                )
            }

            Surface(
                color = ItirafTheme.colors.backgroundCard,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 4.dp
                        )
                    )
                    .height(32.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = statusColor
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentMessageDetailBodyPreview() {
    val mockMessage = SentMessage(
        requestId = "1",
        confessionAuthorUsername = "anonim_yazar",
        initialMessage = "Bugün kütüphanenin önündeki sarman kediye mama verdim, çok tatlıydı.",
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
            SentMessageDetailBody(message = mockMessage)

            Spacer(modifier = Modifier.height(20.dp))

            SentMessageDetailBody(message = mockMessageNoTitle)
        }
    }
}