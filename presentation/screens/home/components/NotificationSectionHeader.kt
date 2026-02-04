package com.itirafapp.android.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SectionHeader(
    title: String,
    isButtonVisible: Boolean = false,
    onButtonClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ItirafTheme.colors.backgroundApp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = ItirafTheme.colors.textSecondary
        )

        if (isButtonVisible) {
            TextButton(
                onClick = onButtonClick,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = stringResource(R.string.notification_mark_all_button),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ItirafTheme.colors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    ItirafAppTheme {
        Column {
            SectionHeader(
                title = "Yeni",
                isButtonVisible = true,
                onButtonClick = {}
            )

            Divider()

            SectionHeader(
                title = "Daha önce",
                isButtonVisible = false
            )
        }
    }
}