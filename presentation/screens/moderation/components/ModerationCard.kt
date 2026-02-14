package com.itirafapp.android.presentation.screens.moderation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.enums.ModerationStatus
import com.itirafapp.android.presentation.components.content.StatusBadge
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationCard(
    data: ModerationData,
    onApproveClick: (Int) -> Unit,
    onRejectClick: (Int) -> Unit
) {
    val (badgeText, badgeColor) = when (data.moderationStatus) {
        ModerationStatus.NEEDS_HUMAN_REVIEW,
        ModerationStatus.PENDING_REVIEW -> {
            Pair(
                stringResource(R.string.confession_status_pending),
                ItirafTheme.colors.statusPending
            )
        }

        ModerationStatus.AI_REJECTED,
        ModerationStatus.HUMAN_REJECTED -> {
            Pair(
                stringResource(R.string.confession_status_rejected),
                ItirafTheme.colors.statusError
            )
        }

        else -> Pair(
            stringResource(R.string.confession_status_unknown),
            ItirafTheme.colors.statusSuccess
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = ItirafTheme.colors.backgroundCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (data.title.isNotEmpty()) {
                        Text(
                            text = data.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            color = ItirafTheme.colors.textPrimary,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Text(
                        text = data.createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = ItirafTheme.colors.textTertiary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = data.message,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    color = ItirafTheme.colors.textSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                InfoRow(
                    label = "${stringResource(R.string.moderation_channel)}:",
                    value = data.channelTitle
                )

                InfoRow(
                    label = "${stringResource(R.string.moderation_sender)}:",
                    value = data.ownerUsername
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = ItirafTheme.colors.dividerColor
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusBadge(text = badgeText, color = badgeColor)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onRejectClick(data.id) },
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            stringResource(R.string.reject),
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = { onApproveClick(data.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ItirafTheme.colors.brandPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            stringResource(R.string.approve),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = ItirafTheme.colors.textSecondary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = ItirafTheme.colors.textSecondary
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ModerationCardPreview() {
    ItirafAppTheme() {
        Column(modifier = Modifier.padding(16.dp)) {
            ModerationCard(
                data = ModerationData(
                    id = 1,
                    title = "Gizli İtiraf",
                    message = "Bu çok uzun bir itiraf metnidir ve beş satırı geçip geçmediğini test etmek için buraya rastgele şeyler yazıyorum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    channelID = 101,
                    channelTitle = "Üniversite İtirafları",
                    ownerID = "u1",
                    ownerUsername = "anonim_kullanici",
                    moderationStatus = ModerationStatus.PENDING_REVIEW,
                    rejectionReason = "",
                    createdAt = "2 saat önce",
                    isNsfw = false
                ),
                onApproveClick = {},
                onRejectClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            ModerationCard(
                data = ModerationData(
                    id = 2,
                    title = "",
                    message = "Kısa bir mesaj.",
                    channelID = 102,
                    channelTitle = "Geyik Muhabbeti",
                    ownerID = "u2",
                    ownerUsername = "test_user",
                    moderationStatus = ModerationStatus.AI_REJECTED,
                    rejectionReason = "Inappropriate content",
                    createdAt = "10 dk önce",
                    isNsfw = true
                ),
                onApproveClick = {},
                onRejectClick = {}
            )
        }
    }
}