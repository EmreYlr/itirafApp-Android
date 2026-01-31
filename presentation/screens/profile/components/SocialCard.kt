package com.itirafapp.android.presentation.screens.profile.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.model.enums.SocialPlatform
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SocialCard(
    social: Link,
    onEditClick: (Link) -> Unit,
    onVisibilityChange: (Boolean) -> Unit
) {
    val switchColors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = ItirafTheme.colors.brandPrimary,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = ItirafTheme.colors.dividerColor,
        uncheckedBorderColor = Color.Transparent
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = ItirafTheme.colors.backgroundCard),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = social.platform.iconResId),
                contentDescription = social.platform.displayName,
                modifier = Modifier
                    .size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = social.username,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = ItirafTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = social.platform.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textSecondary,
                    maxLines = 1,
                    fontSize = 10.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { onEditClick(social) },
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.defaultMinSize(minWidth = 1.dp)
                ) {
                    Text(
                        text = stringResource(R.string.edit),
                        style = MaterialTheme.typography.bodySmall,
                        color = ItirafTheme.colors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Switch(
                    checked = social.visible,
                    onCheckedChange = onVisibilityChange,
                    colors = switchColors,
                    modifier = Modifier.scale(0.8f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SocialCardPreview() {
    ItirafAppTheme {
        Column {
            SocialCard(
                social = Link(
                    id = "1",
                    username = "itirafapp",
                    platform = SocialPlatform.TWITTER,
                    url = "https://twitter.com/itirafapp",
                    visible = false,

                    ),
                onEditClick = {},
                onVisibilityChange = {}
            )

            SocialCard(
                social = Link(
                    id = "1",
                    username = "itiraf.App",
                    platform = SocialPlatform.INSTAGRAM,
                    url = "https://instagram.com/itirafapp",
                    visible = true,

                    ),
                onEditClick = {},
                onVisibilityChange = {}
            )
        }

    }
}