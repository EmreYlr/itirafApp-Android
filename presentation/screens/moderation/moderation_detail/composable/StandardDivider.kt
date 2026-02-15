package com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun StandardDivider() {
    HorizontalDivider(color = ItirafTheme.colors.dividerColor)
    Spacer(modifier = Modifier.height(16.dp))
}