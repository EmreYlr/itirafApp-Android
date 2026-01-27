package com.itirafapp.android.presentation.screens.report

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ReportScreen(
    target: ReportTarget,
    onDismiss: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = target) {
        viewModel.onEvent(ReportEvent.Init(target))
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ReportUiEvent.Dismiss -> {
                    onDismiss()
                }

                is ReportUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    ReportContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportContent(
    state: ReportState,
    onEvent: (ReportEvent) -> Unit
) {
    val maxChar = 500
    val remainingChar = maxChar - state.reason.length

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        Text(
            text = "Şikayet Detayı",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = ItirafTheme.colors.textPrimary,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Lütfen yaşadığınız sorunu aşağıda detaylı bir şekilde açıklayınız.",
            style = MaterialTheme.typography.bodySmall,
            color = ItirafTheme.colors.textSecondary,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.reason,
            onValueChange = { newText ->
                if (newText.length <= maxChar) {
                    onEvent(ReportEvent.ReasonText(newText))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            placeholder = {
                Text(
                    text = "Yaşadığınız sorunu veya şikayetinizi detaylı bir şekilde buraya yazabilirsiniz.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ItirafTheme.colors.textTertiary
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ItirafTheme.colors.brandPrimary,
                unfocusedBorderColor = ItirafTheme.colors.dividerColor,
                cursorColor = ItirafTheme.colors.brandPrimary,
                focusedContainerColor = ItirafTheme.colors.backgroundCard,
                unfocusedContainerColor = ItirafTheme.colors.backgroundCard
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = ItirafTheme.colors.textPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            minLines = 5,
            maxLines = 10
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Kalan: $remainingChar",
            style = MaterialTheme.typography.labelMedium,
            color = if (remainingChar < 50) ItirafTheme.colors.statusError else ItirafTheme.colors.textSecondary,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ItirafTheme.colors.brandPrimary.copy(alpha = 0.3f))
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = ItirafTheme.colors.brandPrimary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Şikayetiniz kalite standartlarımız gereği incelenecek ve en kısa sürede tarafınıza dönüş yapılacaktır.",
                style = MaterialTheme.typography.bodySmall,
                color = ItirafTheme.colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ItirafButton(
            text = "Şikayeti İlet",
            onClick = { onEvent(ReportEvent.SubmitClicked) },
            isLoading = state.isLoading,
            enabled = state.reason.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Preview(
    name = "4. Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewReportContent_Dark() {
    ItirafAppTheme {
        ReportContent(
            state = ReportState(
                reason = "",
                isLoading = false
            ),
            onEvent = {}
        )
    }
}