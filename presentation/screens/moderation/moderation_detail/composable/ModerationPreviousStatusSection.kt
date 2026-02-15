package com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationPreviousStatusSection(rejectionReason: String?) {
    if (!rejectionReason.isNullOrEmpty()) {
        Text(
            text = stringResource(R.string.moderation_detail_rejection_title),
            style = MaterialTheme.typography.titleMedium,
            color = ItirafTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = rejectionReason,
            style = MaterialTheme.typography.bodyMedium,
            color = ItirafTheme.colors.textSecondary
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}