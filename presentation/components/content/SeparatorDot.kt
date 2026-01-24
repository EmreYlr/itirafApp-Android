package com.itirafapp.android.presentation.components.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SeparatorDot(
    modifier: Modifier = Modifier,
    size: Dp = 4.dp,
    horizontalPadding: Dp = 6.dp,
    color: Color = ItirafTheme.colors.textSecondary
) {
    Box(
        modifier = modifier
            .padding(horizontal = horizontalPadding)
            .size(size)
            .background(
                color = color,
                shape = CircleShape
            )
    )
}