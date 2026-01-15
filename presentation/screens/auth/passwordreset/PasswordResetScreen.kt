package com.itirafapp.android.presentation.screens.auth.passwordreset

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.common.ItirafButton
import com.itirafapp.android.presentation.components.common.ItirafTextField
import com.itirafapp.android.presentation.components.common.ItirafTopBar
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun PasswordResetScreen(
    onNavigateBack: () -> Unit,
    viewModel: PasswordResetViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is PasswordResetUiEvent.NavigateToBack -> {
                    onNavigateBack()
                }

                is PasswordResetUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    PasswordResetContent(
        state = state,
        onEvent = viewModel::onEvent,
        onResetClicked = {
            focusManager.clearFocus()
            viewModel.onEvent(PasswordResetEvent.ResetClicked)
        },
        onBackClicked = {
            viewModel.onEvent(PasswordResetEvent.BackClicked)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetContent(
    state: PasswordResetState,
    onEvent: (PasswordResetEvent) -> Unit,
    onResetClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            ItirafTopBar(
                title = stringResource(R.string.password_reset_title),
                canNavigateBack = true,
                onNavigateBack = {
                    focusManager.clearFocus()
                    onBackClicked()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    stringResource(R.string.password_reset_title),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = ItirafTheme.colors.textPrimary
                )

                Text(
                    stringResource(R.string.password_reset_description),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = ItirafTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ItirafTextField(
                value = state.email,
                onValueChange = { onEvent(PasswordResetEvent.EmailChanged(it)) },
                hint = stringResource(R.string.auth_field_email),
                placeholder = stringResource(R.string.auth_field_email_placeholder),
                imeAction = ImeAction.Done,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Mail,
                        contentDescription = "Email Icon",
                        tint = ItirafTheme.colors.textSecondary
                    )
                },
                onAction = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onResetClicked()
                    }
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            ItirafButton(
                text = stringResource(R.string.password_reset_button),
                isLoading = state.isLoading,
                enabled = state.email.isNotBlank(),
                onClick = {
                    focusManager.clearFocus()
                    onResetClicked()
                }
            )

            Spacer(modifier = Modifier.weight(2f))

        }

    }

}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun PasswordResetPreview() {
    ItirafAppTheme {
        PasswordResetContent(
            state = PasswordResetState(),
            onEvent = {},
            onResetClicked = {},
            onBackClicked = {}
        )
    }
}