package com.itirafapp.android.presentation.screens.moderation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.DecisionMode
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationDecisionSegmentedControl(
    selectedMode: DecisionMode,
    onModeSelected: (DecisionMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(50))
            .background(ItirafTheme.colors.backgroundCard)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        SegmentButton(
            text = stringResource(R.string.approve),
            isSelected = selectedMode == DecisionMode.APPROVE,
            selectedColor = ItirafTheme.colors.statusSuccess.copy(alpha = 0.4f),
            onClick = { onModeSelected(DecisionMode.APPROVE) }
        )

        SegmentButton(
            text = stringResource(R.string.reject),
            isSelected = selectedMode == DecisionMode.REJECT,
            selectedColor = ItirafTheme.colors.statusError.copy(alpha = 0.4f),
            onClick = { onModeSelected(DecisionMode.REJECT) }
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ModerationDecisionSegmentedControlPreview() {
    ItirafAppTheme() {
        val (selectedMode, onModeSelected) = remember { mutableStateOf(DecisionMode.APPROVE) }
        ModerationDecisionSegmentedControl(
            selectedMode = selectedMode,
            onModeSelected = onModeSelected
        )
    }
}