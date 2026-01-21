package com.itirafapp.android.presentation.components.common

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.presentation.model.ConfessionUiModel
import com.itirafapp.android.presentation.model.OwnerUiModel
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ConfessionCard(
    confession: ConfessionUiModel,
    onCardClick: (Int) -> Unit,
    onChannelClick: (Int) -> Unit,
    onLikeClick: (Int) -> Unit,
    onCommentClick: (Int) -> Unit,
    onDMRequestClick: (Int) -> Unit,
    onShareClick: (Int) -> Unit
) {
    val displayName = confession.owner.username
    val hasTitle = confession.title.isEmpty()

    val animatedColor by animateColorAsState(
        targetValue = if (confession.liked) ItirafTheme.colors.actionLike else ItirafTheme.colors.textSecondary,
        label = "LikeColorAnimation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick(confession.id) },
        colors = CardDefaults.cardColors(containerColor = ItirafTheme.colors.backgroundApp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = ItirafTheme.colors.textSecondary
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(4.dp)
                        .background(
                            color = ItirafTheme.colors.textSecondary,
                            shape = CircleShape
                        )
                )

                Text(
                    text = confession.createdAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textSecondary
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = confession.channel.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textSecondary,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        onChannelClick(confession.id)
                    }

                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(horizontalAlignment = Alignment.Start) {
                if (!hasTitle) {
                    Text(
                        text = confession.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = ItirafTheme.colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = confession.message,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = ItirafTheme.colors.textSecondary,
                    textAlign = TextAlign.Justify
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick =
                            { onLikeClick(confession.id) },
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            imageVector = if (confession.liked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (confession.liked) "Unlike" else "Like",
                            tint = animatedColor
                        )
                    }
                    AnimatedCounter(
                        modifier = Modifier.padding(2.dp),
                        count = confession.likeCount,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ItirafTheme.colors.textSecondary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick =
                            { onCommentClick(confession.id) },
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ModeComment,
                            contentDescription = "Comment",
                            tint = ItirafTheme.colors.textSecondary
                        )
                    }

                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = "${confession.replyCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ItirafTheme.colors.textSecondary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick =
                            { onDMRequestClick(confession.id) },
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.QuestionAnswer,
                            contentDescription = "DM",
                            tint = ItirafTheme.colors.textSecondary
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick =
                            { onShareClick(confession.id) },
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = ItirafTheme.colors.textSecondary
                        )
                    }
                }
            }
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
fun ConfessionCardPreview() {
    val stateLike = remember { mutableStateOf(false) }
    val likeCount = remember { mutableStateOf(value = 12) }
    ItirafAppTheme {
        ConfessionCard(
            confession = ConfessionUiModel(
                id = 1,
                title = "Bu bir itiraftır. ",
                "Bugün Ankara'ya gittim. Orda bir kız gördüm. Sarı saçlı," +
                        " mavi gözlü çok güzeldi.Hala unutamıyorum.",
                stateLike.value,
                likeCount.value,
                3,
                0,
                "2s önce",
                OwnerUiModel(id = "1", username = "Emre"),
                channel = ChannelData(
                    id = 1,
                    title = "Ankara Üni",
                    description = "Test",
                    imageURL = ""
                ),
                isNsfw = false
            ),
            onCardClick = {},
            onChannelClick = {},
            onLikeClick = {
                stateLike.value = !stateLike.value
                likeCount.value = if (stateLike.value) likeCount.value + 1 else likeCount.value - 1
            },
            onShareClick = {},
            onCommentClick = {},
            onDMRequestClick = {}
        )
    }
}