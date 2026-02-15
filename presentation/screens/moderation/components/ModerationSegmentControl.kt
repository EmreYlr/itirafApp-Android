package com.itirafapp.android.presentation.screens.moderation.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.enums.ModerationFilter
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationSegmentedControl(
    selectedFilter: ModerationFilter,
    onFilterSelected: (ModerationFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .clip(RoundedCornerShape(50))
            .background(ItirafTheme.colors.backgroundCard)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        SegmentButton(
            text = stringResource(R.string.moderation_filter_all),
            isSelected = selectedFilter == ModerationFilter.ALL,
            selectedColor = ItirafTheme.colors.brandPrimary,
            onClick = { onFilterSelected(ModerationFilter.ALL) }
        )

        SegmentButton(
            text = stringResource(R.string.moderation_filter_pending),
            isSelected = selectedFilter == ModerationFilter.PENDING,
            selectedColor = ItirafTheme.colors.statusPending,
            onClick = { onFilterSelected(ModerationFilter.PENDING) }
        )

        SegmentButton(
            text = stringResource(R.string.moderation_filter_rejected),
            isSelected = selectedFilter == ModerationFilter.REJECTED,
            selectedColor = ItirafTheme.colors.statusError,
            onClick = { onFilterSelected(ModerationFilter.REJECTED) }
        )
    }
}

@Composable
fun RowScope.SegmentButton(
    text: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) selectedColor else Color.Transparent,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "ColorAnimation"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(50))
            .background(animatedBackgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = ItirafTheme.colors.pureContrast,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ModerationSegmentedControlPreview() {
    ItirafAppTheme() {
        val (selectedFilter, onFilterSelected) = remember { mutableStateOf(ModerationFilter.ALL) }
        ModerationSegmentedControl(
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected
        )
    }
}