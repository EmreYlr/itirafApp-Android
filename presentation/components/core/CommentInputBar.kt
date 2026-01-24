package com.itirafapp.android.presentation.components.core

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.R
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun CommentInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = ItirafTheme.colors.backgroundApp,
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        Column {
            HorizontalDivider(
                thickness = 0.5.dp,
                color = ItirafTheme.colors.textTertiary.copy(alpha = 0.1f)
            )

            Row(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.navigationBars))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .onFocusChanged { isFocused = it.isFocused }
                        .background(ItirafTheme.colors.backgroundCard, CircleShape)
                        .border(
                            width = 1.dp,
                            color = if (isFocused) ItirafTheme.colors.brandPrimary else Color.Transparent,
                            shape = CircleShape
                        ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = ItirafTheme.colors.textPrimary
                    ),
                    singleLine = true,
                    maxLines = 1,
                    cursorBrush = SolidColor(ItirafTheme.colors.brandPrimary),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.reply_placeholder),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ItirafTheme.colors.textTertiary,
                                    maxLines = 1
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onSendClick,
                    enabled = value.isNotBlank() && !isLoading,
                    modifier = Modifier.size(40.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = ItirafTheme.colors.brandPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (value.isNotBlank()) ItirafTheme.colors.brandPrimary else ItirafTheme.colors.textTertiary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun CommentInputBarPreview() {
    ItirafAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ItirafTheme.colors.backgroundApp)
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column {
                Text(
                    "1. Boş Durum",
                    color = ItirafTheme.colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                CommentInputBar(
                    value = "",
                    onValueChange = {},
                    onSendClick = {},
                    isLoading = false
                )
            }

            Column {
                Text(
                    "2. Dolu Durum",
                    color = ItirafTheme.colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                CommentInputBar(
                    value = "Harika bir itiraf olmuş!",
                    onValueChange = {},
                    onSendClick = {},
                    isLoading = false
                )
            }

            Column {
                Text(
                    "3. Loading Durumu",
                    color = ItirafTheme.colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                CommentInputBar(
                    value = "Yorum gönderiliyor...",
                    onValueChange = {},
                    onSendClick = {},
                    isLoading = true
                )
            }
        }
    }
}