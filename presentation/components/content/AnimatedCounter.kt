package com.itirafapp.android.presentation.components.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedCounter(
    modifier: Modifier,
    count: Int,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified
) {
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            if (targetState > initialState) {
                slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
            } else {
                slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
            }
        },
        label = "CounterAnim"
    ) { number ->
        Text(
            modifier = modifier,
            text = "$number",
            style = style, color = color
        )
    }
}