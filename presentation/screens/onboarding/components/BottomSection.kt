package com.itirafapp.android.presentation.screens.onboarding.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun BottomSection(
    isFirstPage: Boolean,
    isLastPage: Boolean,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isFirstPage) {
            ItirafButton(
                text = stringResource(R.string.back),
                onClick = onBackClick,
                containerColor = ItirafTheme.colors.textSecondary.copy(alpha = 0.2f),
                contentColor = ItirafTheme.colors.textSecondary,
                modifier = Modifier.weight(1f)
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.width(16.dp))

        ItirafButton(
            text = if (isLastPage) stringResource(R.string.start) else stringResource(R.string.next),
            onClick = onNextClick,
            contentColor = Color.White,
            containerColor =
                if (isLastPage) ItirafTheme.colors.brandSecondary
                else ItirafTheme.colors.brandPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomSectionPreview() {
    ItirafAppTheme() {
        BottomSection(
            isFirstPage = false,
            isLastPage = false,
            onBackClick = {},
            onNextClick = {}
        )
    }
}