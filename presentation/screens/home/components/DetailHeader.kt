package com.itirafapp.android.presentation.screens.home.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.presentation.components.content.AnimatedCounter
import com.itirafapp.android.presentation.components.content.SeparatorDot
import com.itirafapp.android.presentation.components.core.MoreActionMenu
import com.itirafapp.android.presentation.model.ConfessionDetailUiModel
import com.itirafapp.android.presentation.model.OwnerUiModel
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.noRippleClickable
import com.itirafapp.android.util.state.UiText

@Composable
fun DetailHeader(
    confessionDetail: ConfessionDetailUiModel,
    isAdmin: Boolean = false,
    onLikeClick: (Int) -> Unit,
    onCommentClick: () -> Unit,
    onDMRequestClick: (Int) -> Unit,
    onShareClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onReportClick: (Int) -> Unit,
    onBlockClick: (String) -> Unit,
    onAdminClick: (Int, Boolean) -> Unit
) {
    val displayName = confessionDetail.owner.username.asString()
    val hasTitle = confessionDetail.title.isNotEmpty()

    var menuExpanded by remember { mutableStateOf(false) }

    val animatedColor by animateColorAsState(
        targetValue =
            if (confessionDetail.liked) ItirafTheme.colors.actionLike
            else ItirafTheme.colors.textSecondary,
        label = "LikeColorAnimation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ItirafTheme.colors.backgroundApp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        color = ItirafTheme.colors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    SeparatorDot()

                    Text(
                        text = confessionDetail.channel.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = ItirafTheme.colors.textSecondary,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(0.7f, fill = false)
                    )

                    SeparatorDot()

                    Text(
                        text = confessionDetail.createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = ItirafTheme.colors.textSecondary,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More Options",
                        tint = ItirafTheme.colors.textSecondary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { menuExpanded = true }
                    )

                    MoreActionMenu(
                        expanded = menuExpanded,
                        onDismiss = { menuExpanded = false },
                        isMine = confessionDetail.isMine,
                        onDelete = { onDeleteClick(confessionDetail.id) },
                        onReport = { onReportClick(confessionDetail.id) },
                        onBlock = { onBlockClick(confessionDetail.owner.id) }
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))
            Column(horizontalAlignment = Alignment.Start) {
                if (hasTitle) {
                    Text(
                        text = confessionDetail.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = ItirafTheme.colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = confessionDetail.message,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = ItirafTheme.colors.textSecondary
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 1. LIKE
                    Icon(
                        imageVector = if (confessionDetail.liked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (confessionDetail.liked) "Unlike" else "Like",
                        tint = animatedColor,
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable { onLikeClick(confessionDetail.id) }
                    )

                    AnimatedCounter(
                        modifier = Modifier.padding(2.dp),
                        count = confessionDetail.likeCount,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ItirafTheme.colors.textSecondary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // 2. COMMENT
                    Icon(
                        imageVector = Icons.Outlined.ModeComment,
                        contentDescription = "Comment",
                        tint = ItirafTheme.colors.textSecondary,
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable { onCommentClick() }
                    )

                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = "${confessionDetail.replyCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ItirafTheme.colors.textSecondary
                    )

                    // 3. DM
                    if (!confessionDetail.isMine) {
                        Spacer(modifier = Modifier.width(16.dp))

                        Icon(
                            imageVector = Icons.Outlined.QuestionAnswer,
                            contentDescription = "DM",
                            tint = ItirafTheme.colors.textSecondary,
                            modifier = Modifier
                                .size(24.dp)
                                .noRippleClickable { onDMRequestClick(confessionDetail.id) }
                        )
                    }

                    if (isAdmin) {
                        Spacer(modifier = Modifier.width(16.dp))

                        Icon(
                            imageVector = Icons.Outlined.AdminPanelSettings,
                            contentDescription = "Admin",
                            tint = ItirafTheme.colors.textSecondary,
                            modifier = Modifier
                                .size(24.dp)
                                .noRippleClickable {
                                    onAdminClick(
                                        confessionDetail.id,
                                        confessionDetail.isNsfw
                                    )
                                }
                        )
                    }


                    Spacer(modifier = Modifier.weight(1f))

                    // 4. SHARE
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = ItirafTheme.colors.textSecondary,
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable { onShareClick(confessionDetail.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                color = ItirafTheme.colors.dividerColor,
                thickness = 0.5.dp
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun DetailHeaderPreview() {
    ItirafAppTheme {
        DetailHeader(
            confessionDetail = ConfessionDetailUiModel(
                id = 123,
                title = "Ofiste yaşadığım garip olay. ",
                message = "Bugün patronun kahvesine yanlışlıkla tuz attım ve fark etmedi.",
                liked = true,
                likeCount = 42,
                replyCount = 5,
                shareCount = 2,
                createdAt = "2s önce",
                owner = OwnerUiModel(
                    id = "1",
                    username = UiText.DynamicString("Emre Yeler")
                ),
                channel = ChannelData(
                    id = 1,
                    title = "Ankara Bilimler Bİlmem Ne Üni",
                    description = "Test",
                    imageURL = ""
                ),
                shortlink = "itiraf.app/x9s2",
                replies = emptyList(),
                isNsfw = false,
                isMine = false
            ),
            onLikeClick = {},
            onCommentClick = {},
            onDMRequestClick = {},
            onShareClick = {},
            onDeleteClick = {},
            onReportClick = {},
            onBlockClick = {},
            isAdmin = true,
            onAdminClick = { _, _ -> }
        )
    }
}