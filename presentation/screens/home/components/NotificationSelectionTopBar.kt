package com.itirafapp.android.presentation.screens.home.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.itirafapp.android.R
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSelectionTopBar(
    selectedCount: Int,
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.notification_selected, selectedCount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ItirafTheme.colors.textPrimary
            )
        },
        navigationIcon = {
            TextButton(onClick = onCancelClick) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = ItirafTheme.colors.brandPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        actions = {
            TextButton(
                onClick = onDeleteClick,
                enabled = selectedCount > 0
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    color = if (selectedCount > 0) ItirafTheme.colors.actionLike else ItirafTheme.colors.textTertiary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ItirafTheme.colors.backgroundApp
        )
    )
}