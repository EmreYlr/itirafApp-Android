package com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationHeaderSection(data: ModerationData?) {
    data?.title?.let { title ->
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ItirafTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    Text(
        text = data?.message ?: "",
        style = MaterialTheme.typography.bodyMedium,
        color = ItirafTheme.colors.textSecondary
    )

    Spacer(modifier = Modifier.height(16.dp))
}