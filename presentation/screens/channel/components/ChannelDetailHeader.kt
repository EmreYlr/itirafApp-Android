package com.itirafapp.android.presentation.screens.channel.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itirafapp.android.R
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ChannelDetailHeader(
    title: String,
    isFollowed: Boolean,
    onFollowClick: () -> Unit
) {
    val initials = title.trim().take(2).uppercase()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(72.dp)
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = ItirafTheme.colors.pureContrast
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = ItirafTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

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
            modifier = Modifier
                .height(36.dp)
                .fillMaxWidth(0.6f),
            contentPadding = PaddingValues(horizontal = 40.dp, vertical = 0.dp)
        ) {
            Text(
                text =
                    if (isFollowed) stringResource(R.string.channel_button_following)
                    else stringResource(R.string.channel_button_follow),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }

    HorizontalDivider(
        color = ItirafTheme.colors.dividerColor
    )
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun ChannelDetailHeaderPreview() {
    ItirafAppTheme {
        Column {
            ChannelDetailHeader(
                title = "Ankara Üniversitesi",
                isFollowed = false,
                onFollowClick = {}
            )
            ChannelDetailHeader(
                title = "Harran Üniversitesi",
                isFollowed = true,
                onFollowClick = {}
            )
        }
    }

}
