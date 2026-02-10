package com.itirafapp.android.presentation.components.core

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun EmptyStateView(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    message: String,
    buttonText: String? = null,
    onButtonClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = ItirafTheme.colors.backgroundCard,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = ItirafTheme.colors.dividerColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ItirafTheme.colors.brandPrimary,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.titleSmall,
            color = ItirafTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        if (buttonText != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ItirafTheme.colors.brandPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.defaultMinSize(minWidth = 140.dp)
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EmptyStateViewPreview() {
    ItirafAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmptyStateView(
                icon = Icons.Default.WifiOff,
                message = "İnternet bağlantısı yok.\nLütfen tekrar deneyin.",
                buttonText = "Tekrar Dene",
                onButtonClick = {}
            )

            Spacer(modifier = Modifier.height(50.dp))
            Divider()
            Spacer(modifier = Modifier.height(50.dp))


            EmptyStateView(
                icon = Icons.Default.ChatBubbleOutline,
                message = "Henüz yorum yapılmamış.\nİlk yorumu sen yap!",
                buttonText = null
            )
        }
    }
}