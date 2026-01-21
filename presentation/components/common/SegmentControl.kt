package com.itirafapp.android.presentation.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(ItirafTheme.colors.backgroundApp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, title ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onIndexChange(index)
                    }
            ) {
                Text(
                    text = title,
                    modifier = Modifier.align(Alignment.Center),
                    color = if (isSelected) ItirafTheme.colors.textPrimary else ItirafTheme.colors.textSecondary,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    style = if (isSelected) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            color = if (isSelected)
                                ItirafTheme.colors.brandPrimary
                            else
                                ItirafTheme.colors.dividerColor
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItirafSegmentedControlPreview() {
    val selectedIndex = remember { mutableStateOf(0) }
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(ItirafTheme.colors.backgroundApp)
        ) {
            SegmentedControl(
                items = listOf("Akış", "Takip Edilenler"),
                selectedIndex = selectedIndex.value,
                onIndexChange = { selectedIndex.value = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            SegmentedControl(
                items = listOf("Akış", "Takip Edilenler"),
                selectedIndex = 1,
                onIndexChange = {}
            )
        }
    }
}
