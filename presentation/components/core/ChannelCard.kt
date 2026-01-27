package com.itirafapp.android.presentation.components.core

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ChannelCard(
    channel: ChannelData,
    isFollowed: Boolean,
    onFollowClick: () -> Unit,
    onClick: () -> Unit
) {
    val linkColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val initials = channel.title.trim()
        .take(2)
        .uppercase()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(
                    0.6.dp,
                    color = ItirafTheme.colors.dividerColor,
                    shape = CircleShape
                )
                .background(ItirafTheme.colors.backgroundCard),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = linkColor
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = channel.title,
            style = MaterialTheme.typography.bodyMedium,
            color = ItirafTheme.colors.textPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onFollowClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    if (isFollowed) ItirafTheme.colors.brandPrimary.copy(alpha = 0.2f)
                    else ItirafTheme.colors.brandSecondary,
                contentColor =
                    if (isFollowed) ItirafTheme.colors.brandPrimary
                    else Color.White
            ),
            modifier = Modifier.height(36.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
        ) {
            Text(
                text =
                    if (isFollowed) stringResource(R.string.channel_button_following)
                    else stringResource(R.string.channel_button_follow),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun PreviewChannelCard() {
    ItirafAppTheme {
        val mockChannel = ChannelData(
            id = 1,
            title = "Ankara Üniversitesi",
            description = "Kampüs itirafları",
            imageURL = null
        )

        Column {
            ChannelCard(
                channel = mockChannel,
                isFollowed = false,
                onFollowClick = {},
                onClick = {}
            )
            ChannelCard(
                channel = mockChannel.copy(title = "Hacettepe Üniversitesi"),
                isFollowed = true,
                onFollowClick = {},
                onClick = {}
            )
        }
    }
}