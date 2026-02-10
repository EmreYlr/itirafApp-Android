package com.itirafapp.android.presentation.screens.home.dm_request

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun DMRequestScreen(
    targetId: Int,
    onDismiss: () -> Unit,
    viewModel: DMRequestViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = targetId) {
        viewModel.onEvent(DMRequestEvent.Init(targetId))
    }

    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is DMRequestUiEvent.Dismiss -> {
                    onDismiss()
                }

                is DMRequestUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    DMRequestContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DMRequestContent(
    state: DMRequestState,
    onEvent: (DMRequestEvent) -> Unit
) {
    val switchColors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = ItirafTheme.colors.brandPrimary,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = ItirafTheme.colors.dividerColor,
        uncheckedBorderColor = Color.Transparent
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .imePadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.dm_request_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = ItirafTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.dm_request_description),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = ItirafTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.dm_request_your_message),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = ItirafTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.initialMessage,
                onValueChange = { onEvent(DMRequestEvent.MessageChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text(
                        stringResource(R.string.dm_request_placeholder),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal,
                        color = ItirafTheme.colors.textSecondary,
                    )
                },
                isError = state.error != null
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.dm_request_share_social),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = ItirafTheme.colors.textPrimary,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = state.shareSocialLinks,
                onCheckedChange = { isChecked ->
                    onEvent(DMRequestEvent.ShareLinksToggled(isChecked))
                },
                colors = switchColors
            )
        }

        HorizontalDivider()

        Spacer(modifier = Modifier.height(24.dp))

        ItirafButton(
            text = stringResource(R.string.dm_request_button),
            isLoading = state.isLoading,
            enabled = state.initialMessage.isNotBlank(),
            onClick = {
                onEvent(DMRequestEvent.SubmitClicked)
            }
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun DMRequestScreenPreview() {
    ItirafAppTheme {
        DMRequestContent(
            state = DMRequestState(),
            onEvent = {}
        )
    }
}