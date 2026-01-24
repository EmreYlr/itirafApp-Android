package com.itirafapp.android.presentation.components.core

import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.presentation.components.content.SeparatorDot
import com.itirafapp.android.presentation.model.OwnerUiModel
import com.itirafapp.android.presentation.model.ReplyUiModel
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.state.UiText

@Composable
fun ReplyCard(
    reply: ReplyUiModel,
    onMoreClicked: (Int) -> Unit,
) {
    val displayName = reply.owner.username.asString()

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ItirafTheme.colors.backgroundCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = ItirafTheme.colors.textTertiary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = ItirafTheme.colors.backgroundApp,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
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
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ItirafTheme.colors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        SeparatorDot()

                        Text(
                            text = reply.createdAt,
                            style = MaterialTheme.typography.bodySmall,
                            color = ItirafTheme.colors.textTertiary,
                            maxLines = 1
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More Options",
                        tint = ItirafTheme.colors.textSecondary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onMoreClicked(reply.id) }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = reply.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ItirafTheme.colors.textSecondary,
                    textAlign = TextAlign.Start
                )
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
fun ReplyCardPreview() {
    ItirafAppTheme {
        ReplyCard(
            reply = ReplyUiModel(
                id = 1,
                message = "Bu bir yorumdur. Bu yüzden dolayı çok fazla önemseyebilirsiniz.",
                owner = OwnerUiModel(
                    id = "1",
                    username = UiText.DynamicString("Emre")
                ),
                createdAt = "2sa önce"
            ),
            onMoreClicked = {}
        )
    }
}