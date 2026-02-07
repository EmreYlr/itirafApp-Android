package com.itirafapp.android.presentation.screens.message.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.domain.model.MessageData
import com.itirafapp.android.presentation.model.ChatUiItem
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ChatBubble(
    item: ChatUiItem
) {
    val message = item.message
    val showTime = item.showTime
    val isFromMe = message.isMyMessage

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val maxBubbleWidth = screenWidth * 0.7f

    val cornerRadius = 16.dp
    val tailRadius = 2.dp

    val bubbleShape = if (showTime) {
        if (isFromMe) {
            RoundedCornerShape(
                topStart = cornerRadius,
                topEnd = cornerRadius,
                bottomStart = cornerRadius,
                bottomEnd = tailRadius
            )
        } else {
            RoundedCornerShape(
                topStart = cornerRadius,
                topEnd = cornerRadius,
                bottomStart = tailRadius,
                bottomEnd = cornerRadius
            )
        }
    } else {
        RoundedCornerShape(cornerRadius)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = if (showTime) 4.dp else 1.dp),
        horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isFromMe) ItirafTheme.colors.brandSecondary else ItirafTheme.colors.backgroundCard,
            contentColor = if (isFromMe) Color.White else ItirafTheme.colors.textPrimary,
            shape = bubbleShape,
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = maxBubbleWidth)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (showTime) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message.createdAt,
                style = MaterialTheme.typography.labelSmall,
                color = ItirafTheme.colors.textTertiary,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ChatBubblePreview() {
    val chatUiItem = ChatUiItem(
        message = MessageData(
            id = 1,
            content = "Merhaba nasılsın",
            createdAt = "1 saat önce",
            isMyMessage = true,
            isSeen = false,
            seenAt = null
        ),
        showTime = true
    )
    ItirafAppTheme {
        Column {
            chatUiItem

            ChatBubble(
                chatUiItem
            )
        }
    }
}