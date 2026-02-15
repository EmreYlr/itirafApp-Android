package com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.presentation.screens.moderation.components.ModerationDetailRow

@Composable
fun ModerationMetadataSection(data: ModerationData?) {
    ModerationDetailRow(
        icon = Icons.Outlined.CalendarToday,
        text = data?.createdAt ?: ""
    )
    ModerationDetailRow(
        icon = Icons.Outlined.Tag,
        text = data?.channelTitle ?: ""
    )
    ModerationDetailRow(
        icon = Icons.Outlined.Person,
        text = data?.ownerUsername ?: ""
    )

    Spacer(modifier = Modifier.height(16.dp))
}