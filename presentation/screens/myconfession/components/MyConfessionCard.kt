package com.itirafapp.android.presentation.screens.myconfession.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.enums.ConfessionDisplayStatus
import com.itirafapp.android.presentation.components.content.StatusBadge
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.displayStatus
import com.itirafapp.android.util.extension.toTruncatedAnnotatedString

@Composable
fun MyConfessionCard(
    confession: MyConfessionData,
    onCardClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit
) {
    val hasTitle = !confession.title.isNullOrBlank()
    val seeMoreColor = ItirafTheme.colors.brandPrimary
    val readMoreText = stringResource(id = R.string.confession_read_more)

    val messageText = remember(confession.message) {
        confession.message.toTruncatedAnnotatedString(
            limit = 300,
            seeMoreColor = seeMoreColor,
            seeMoreText = readMoreText
        )
    }

    val (statusText, statusColor) = when (confession.displayStatus) {
        ConfessionDisplayStatus.APPROVED -> Pair("Aktif", ItirafTheme.colors.statusSuccess)
        ConfessionDisplayStatus.REJECTED -> Pair("Reddedildi", ItirafTheme.colors.statusError)
        ConfessionDisplayStatus.IN_REVIEW -> Pair("Onay Bekliyor", ItirafTheme.colors.statusPending)
        else -> Pair("Bilinmiyor", ItirafTheme.colors.textSecondary)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick(confession.id) },
        colors = CardDefaults.cardColors(containerColor = ItirafTheme.colors.backgroundCard),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(
                    text = statusText,
                    color = statusColor
                )

                if (confession.isNsfw) {
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(
                        text = "Hassas İçerik",
                        color = ItirafTheme.colors.sensitiveAccent
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = confession.createdAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textSecondary,
                    fontWeight = FontWeight.Light,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(horizontalAlignment = Alignment.Start) {
                if (hasTitle) {
                    Text(
                        text = confession.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = ItirafTheme.colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = messageText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = ItirafTheme.colors.textSecondary,
                    textAlign = TextAlign.Justify
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = ItirafTheme.colors.textSecondary,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${confession.likeCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ItirafTheme.colors.textSecondary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Outlined.ModeComment,
                    contentDescription = null,
                    tint = ItirafTheme.colors.textSecondary,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${confession.replyCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ItirafTheme.colors.textSecondary
                )

                Spacer(modifier = Modifier.weight(1f))

                if (confession.displayStatus == ConfessionDisplayStatus.REJECTED) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = ItirafTheme.colors.statusError.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onEditClick(confession.id) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Düzenle",
                            style = MaterialTheme.typography.labelMedium,
                            color = ItirafTheme.colors.statusError,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
