package com.itirafapp.android.presentation.screens.message.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.PersonOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun InboxSocialRow(
    social: Link?,
    onClick: () -> Unit = {}
) {
    val isAnonymous = social == null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .then(if (!isAnonymous) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(containerColor = ItirafTheme.colors.backgroundCard),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isAnonymous) {
                Icon(
                    imageVector = Icons.Rounded.PersonOff,
                    contentDescription = null,
                    tint = ItirafTheme.colors.textPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = social.platform.iconResId),
                    contentDescription = social.platform.displayName,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (isAnonymous) stringResource(R.string.social_anonymous_title) else social.username,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = ItirafTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = if (isAnonymous) stringResource(R.string.social_anonymous_description) else social.platform.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textSecondary,
                    maxLines = 1,
                    fontSize = 10.sp
                )
            }

            if (!isAnonymous) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = ItirafTheme.colors.textTertiary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}