package com.itirafapp.android.presentation.screens.profile.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itirafapp.android.domain.model.NotificationEventType
import com.itirafapp.android.presentation.model.NotificationUiModel
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.state.UiText

@Composable
fun NotificationRow(
    item: NotificationUiModel,
    isMasterEnabled: Boolean,
    onSwitchChanged: (Boolean) -> Unit
) {
    val rowAlpha = if (isMasterEnabled) 1f else 0.5f

    val switchColors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = ItirafTheme.colors.brandPrimary,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = ItirafTheme.colors.dividerColor,
        uncheckedBorderColor = Color.Transparent,
        disabledCheckedTrackColor = ItirafTheme.colors.brandPrimary.copy(alpha = 0.4f),
        disabledUncheckedTrackColor = ItirafTheme.colors.dividerColor.copy(alpha = 0.4f),
        disabledCheckedThumbColor = Color.White.copy(alpha = 0.8f),
        disabledUncheckedThumbColor = Color.White.copy(alpha = 0.8f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .alpha(rowAlpha)
        ) {
            Text(
                text = item.title.asString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = ItirafTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.description.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = ItirafTheme.colors.textSecondary,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Switch(
            checked = item.isEnabled,
            onCheckedChange = onSwitchChanged,
            enabled = isMasterEnabled,
            colors = switchColors,
            modifier = Modifier.alpha(rowAlpha)
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotificationRowPreview() {
    ItirafAppTheme() {
        NotificationRow(
            item = NotificationUiModel(
                type = NotificationEventType.CONFESSION,
                title = UiText.DynamicString("İtiraf"),
                description = UiText.DynamicString("İtiraflarından haberdar ol"),
                isEnabled = true
            ),
            isMasterEnabled = true,
            onSwitchChanged = {}
        )
    }
}