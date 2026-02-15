package com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.itirafapp.android.R
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ApproveFormContent(
    isNsfw: Boolean,
    onToggleNsfw: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.moderation_detail_nsfw_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = ItirafTheme.colors.textPrimary
        )

        Switch(
            checked = isNsfw,
            onCheckedChange = onToggleNsfw,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ItirafTheme.colors.brandPrimary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = ItirafTheme.colors.dividerColor,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}