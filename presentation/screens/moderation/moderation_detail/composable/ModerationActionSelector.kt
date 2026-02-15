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
import com.itirafapp.android.presentation.screens.moderation.components.ModerationDecisionSegmentedControl
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.DecisionMode
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationActionSelector(
    decisionMode: DecisionMode,
    onModeSelected: (DecisionMode) -> Unit
) {
    Text(
        text = stringResource(R.string.moderation_detail_action_title),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = ItirafTheme.colors.textPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))

    ModerationDecisionSegmentedControl(
        selectedMode = decisionMode,
        onModeSelected = onModeSelected
    )
}