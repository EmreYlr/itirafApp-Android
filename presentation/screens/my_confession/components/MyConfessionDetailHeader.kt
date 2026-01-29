package com.itirafapp.android.presentation.screens.my_confession.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.enums.ConfessionDisplayStatus
import com.itirafapp.android.domain.model.enums.ModerationStatus
import com.itirafapp.android.presentation.components.content.AnimatedCounter
import com.itirafapp.android.presentation.components.content.SeparatorDot
import com.itirafapp.android.presentation.components.content.StatusBadge
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.displayStatus
import com.itirafapp.android.util.extension.noRippleClickable

@Composable
fun MyConfessionDetailHeader(
    myConfessionDetail: MyConfessionData,
    onLikeClick: (Int) -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
) {
    val (statusText, statusColor) = when (myConfessionDetail.displayStatus) {
        ConfessionDisplayStatus.APPROVED -> Pair(
            stringResource(R.string.confession_status_active),
            ItirafTheme.colors.statusSuccess
        )

        ConfessionDisplayStatus.REJECTED -> Pair(
            stringResource(R.string.confession_status_rejected),
            ItirafTheme.colors.statusError
        )

        ConfessionDisplayStatus.IN_REVIEW -> Pair(
            stringResource(R.string.confession_status_pending),
            ItirafTheme.colors.statusPending
        )

        else -> Pair(
            stringResource(R.string.confession_status_unknown),
            ItirafTheme.colors.textSecondary
        )
    }

    val animatedLikeColor by animateColorAsState(
        targetValue =
            if (myConfessionDetail.isLiked) ItirafTheme.colors.actionLike
            else ItirafTheme.colors.textSecondary,
        label = "LikeColor"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.status_label),
                style = MaterialTheme.typography.bodyMedium,
                color = ItirafTheme.colors.textPrimary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(8.dp))

            StatusBadge(text = statusText, color = statusColor)

            if (myConfessionDetail.isNsfw) {
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(
                    text = stringResource(R.string.confession_nsfw),
                    color = ItirafTheme.colors.sensitiveAccent
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (myConfessionDetail.displayStatus == ConfessionDisplayStatus.REJECTED) {
                Box(
                    modifier = Modifier
                        .background(
                            color = ItirafTheme.colors.statusError.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onEditClick(myConfessionDetail.id) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.edit),
                        style = MaterialTheme.typography.labelMedium,
                        color = ItirafTheme.colors.statusError,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = CardDefaults.cardColors(containerColor = ItirafTheme.colors.backgroundCard),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = myConfessionDetail.channel.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = ItirafTheme.colors.textSecondary,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    SeparatorDot()

                    Text(
                        text = myConfessionDetail.createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = ItirafTheme.colors.textSecondary,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (!myConfessionDetail.title.isNullOrBlank()) {
                    Text(
                        text = myConfessionDetail.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = ItirafTheme.colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = myConfessionDetail.message,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = ItirafTheme.colors.textSecondary,
                    textAlign = TextAlign.Justify
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Like
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (myConfessionDetail.isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = animatedLikeColor,
                    modifier = Modifier
                        .size(26.dp)
                        .noRippleClickable { onLikeClick(myConfessionDetail.id) }
                )
                Spacer(modifier = Modifier.width(4.dp))
                AnimatedCounter(
                    count = myConfessionDetail.likeCount,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ItirafTheme.colors.textSecondary,
                    modifier = Modifier.padding(2.dp),
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Comment
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.ModeComment,
                    contentDescription = null,
                    tint = ItirafTheme.colors.textSecondary,
                    modifier = Modifier
                        .size(24.dp)
                        .noRippleClickable { onCommentClick() }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${myConfessionDetail.replyCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ItirafTheme.colors.textSecondary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Share
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = null,
                tint = ItirafTheme.colors.textSecondary,
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable { onShareClick(myConfessionDetail.id) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            color = ItirafTheme.colors.dividerColor
        )
    }
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun MyConfessionDetailHeaderPreview() {

    val mockChannel = ChannelData(
        id = 1,
        title = "İtiraflar",
        description = "Genel",
        imageURL = null
    )

    val approvedConfession = MyConfessionData(
        id = 1,
        title = "Harika Bir Gün",
        message = "Bugün hava çok güzeldi, sahilde yürüyüş yaparken eski bir dostumu gördüm. Hayat sürprizlerle dolu.",
        isLiked = true,
        likeCount = 120,
        replyCount = 5,
        shareCount = 0,
        createdAt = "2 saat önce",
        channel = mockChannel,
        reply = emptyList(),
        rejectionReason = null,
        violations = null,
        moderationStatus = ModerationStatus.HUMAN_APPROVED,
        isNsfw = true
    )

    val rejectedConfession = approvedConfession.copy(
        id = 2,
        title = "Öfkeli Anım",
        message = "Bu itiraf kurallara uymadığı için reddedildi. Lütfen düzenleyip tekrar gönderiniz.",
        moderationStatus = ModerationStatus.HUMAN_REJECTED,
        likeCount = 0,
        replyCount = 0,
        isNsfw = false
    )

    val pendingNsfwConfession = approvedConfession.copy(
        id = 3,
        title = null,
        message = "Bu içerik hassas olabilir ve şu an moderatör onayı beklemektedir.",
        moderationStatus = ModerationStatus.PENDING_REVIEW,
        isNsfw = false,
        likeCount = 0,
        replyCount = 0
    )

    ItirafAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ItirafTheme.colors.backgroundApp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MyConfessionDetailHeader(
                myConfessionDetail = approvedConfession,
                onLikeClick = {},
                onCommentClick = {},
                onShareClick = {},
                onEditClick = {},
            )

            MyConfessionDetailHeader(
                myConfessionDetail = rejectedConfession,
                onLikeClick = {},
                onCommentClick = {},
                onShareClick = {},
                onEditClick = {},
            )

            MyConfessionDetailHeader(
                myConfessionDetail = pendingNsfwConfession,
                onLikeClick = {},
                onCommentClick = {},
                onShareClick = {},
                onEditClick = {},
            )
        }
    }
}