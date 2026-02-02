package com.itirafapp.android.presentation.screens.profile.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.domain.model.enums.SettingActionType
import com.itirafapp.android.presentation.model.SettingItemUiModel
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.state.UiText

@Composable
fun SettingsRow(
    item: SettingItemUiModel,
    onClick: () -> Unit
) {
    val isRowClickable = item.isEnabled || item.forceClickable

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isRowClickable, onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint =
                if (item.isEnabled) ItirafTheme.colors.brandPrimary
                else ItirafTheme.colors.textTertiary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = item.title.asString(),
            style = MaterialTheme.typography.titleSmall,
            color =
                if (item.isEnabled) ItirafTheme.colors.textPrimary
                else ItirafTheme.colors.textTertiary,
            modifier = Modifier.weight(1f)
        )

        if (item.isEnabled && item.showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = ItirafTheme.colors.textSecondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsRowPreview() {
    ItirafAppTheme() {
        SettingsRow(
            item = SettingItemUiModel(
                title = UiText.DynamicString("Profil"),
                icon = Icons.Default.Person,
                type = SettingActionType.EDIT_PROFILE,
                isEnabled = false,
                showChevron = true
            ),
            onClick = {

            }
        )
    }
}