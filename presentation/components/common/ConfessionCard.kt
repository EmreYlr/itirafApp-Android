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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.ChannelData
import com.itirafapp.android.presentation.model.ConfessionUiModel
import com.itirafapp.android.presentation.model.OwnerUiModel
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.UiText
import com.itirafapp.android.util.toTruncatedAnnotatedString

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
    val displayName = confession.owner.username.asString()
    val hasTitle = confession.title.isNotEmpty()
    val seeMoreColor = ItirafTheme.colors.brandPrimary
    val readMoreText = stringResource(id = R.string.confession_read_more)

    val animatedColor by animateColorAsState(
        targetValue =
            if (confession.liked) ItirafTheme.colors.actionLike
            else ItirafTheme.colors.textSecondary,
        label = "LikeColorAnimation"
    )

    val messageText = remember(confession.message) {
        confession.message.toTruncatedAnnotatedString(
            limit = 300,
            seeMoreColor = seeMoreColor,
            seeMoreText = readMoreText
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick(confession.id) },
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
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically
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
                        color = ItirafTheme.colors.textSecondary,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = confession.channel.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textSecondary,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(0.7f, fill = false)
                        .clickable { onChannelClick(confession.id) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 1. LIKE
                    Icon(
                        imageVector = if (confession.liked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (confession.liked) "Unlike" else "Like",
                        tint = animatedColor,
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable { onLikeClick(confession.id) }
                    )

                    AnimatedCounter(
                        modifier = Modifier.padding(2.dp),
                        count = confession.likeCount,
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
                            .noRippleClickable { onCommentClick(confession.id) }
                    )

                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = "${confession.replyCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ItirafTheme.colors.textSecondary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // 3. DM
                    Icon(
                        imageVector = Icons.Outlined.QuestionAnswer,
                        contentDescription = "DM",
                        tint = ItirafTheme.colors.textSecondary,
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable { onDMRequestClick(confession.id) }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // 4. SHARE
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = ItirafTheme.colors.textSecondary,
                        modifier = Modifier
                            .size(24.dp)
                            .noRippleClickable { onShareClick(confession.id) }
                    )
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
                title = "Bu bir itiraftır. Bu yüzden dolayı çok fazla önemseyebilirsiniz.",
                message = "Bugün Ankara'ya gittim. Orda bir kız gördüm. Sarı saçlı," +
                        " mavi gözlü çok güzeldi.Hala unutamıyorum.",
                liked = stateLike.value,
                likeCount = likeCount.value,
                replyCount = 3,
                createdAt = "2s önce",
                owner = OwnerUiModel(
                    id = "1",
                    username = UiText.DynamicString("Emre")
                ),
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