package com.itirafapp.android.presentation.screens.auth.register

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.common.ItirafButton
import com.itirafapp.android.presentation.components.common.ItirafTextField
import com.itirafapp.android.presentation.components.common.TopBar
import com.itirafapp.android.presentation.screens.auth.components.TermText
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.Constants
import com.itirafapp.android.util.openUrlSafe
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val colorParams = ItirafTheme.colors.brandPrimary.toArgb()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is RegisterUiEvent.NavigateToLogin -> {
                    onNavigateBack()
                }

                is RegisterUiEvent.ShowTermsDialog -> {
                    openUrlSafe(context, Constants.TERMS_URL, colorParams)
                }

                is RegisterUiEvent.ShowPrivacyPolicyDialog -> {
                    openUrlSafe(context, Constants.PRIVACY_URL, colorParams)
                }

                is RegisterUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    RegisterContent(
        state = state,
        onEvent = viewModel::onEvent,
        onRegisterClick = {
            focusManager.clearFocus()
            viewModel.onEvent(RegisterEvent.RegisterClicked)
        },
        onLoginClick = {
            viewModel.onEvent(RegisterEvent.LoginClicked)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.register),
                canNavigateBack = true,
                onNavigateBack = {
                    focusManager.clearFocus()
                    onLoginClick()
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
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.register_title),
                style = MaterialTheme.typography.titleLarge,
                color = ItirafTheme.colors.textPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Text(
                stringResource(R.string.register_body),
                style = MaterialTheme.typography.bodyMedium,
                color = ItirafTheme.colors.textSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(24.dp))

            ItirafTextField(
                value = state.email,
                onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
                hint = stringResource(R.string.auth_field_email),
                keyboardType = KeyboardType.Email,
                placeholder = stringResource(R.string.auth_field_email_placeholder),
                imeAction = ImeAction.Next,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Mail,
                        contentDescription = "Email Icon",
                        tint = ItirafTheme.colors.textSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ItirafTextField(
                value = state.password,
                onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                hint = stringResource(R.string.auth_field_password),
                isPassword = true,
                placeholder = "*********",
                imeAction = ImeAction.Done,
                onAction = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onLoginClick()
                    }
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            val switchColors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ItirafTheme.colors.brandPrimary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = ItirafTheme.colors.dividerColor,
                uncheckedBorderColor = Color.Transparent
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TermText(
                    fullTextRes = R.string.accept_terms_mask,
                    linkTextRes = R.string.privacy_policy_link,
                    onLinkClick = {
                        onEvent(RegisterEvent.OpenPrivacyPolicy)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Switch(
                    checked = state.isPrivacyAccepted,
                    onCheckedChange = { isChecked ->
                        onEvent(RegisterEvent.PrivacyPolicyChanged(isChecked))
                    },
                    colors = switchColors
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TermText(
                    fullTextRes = R.string.accept_terms_mask,
                    linkTextRes = R.string.terms_conditions_link,
                    onLinkClick = {
                        onEvent(RegisterEvent.OpenTermsOfUse)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Switch(
                    checked = state.isTermsAccepted,
                    onCheckedChange = { isChecked ->
                        onEvent(RegisterEvent.TermsChanged(isChecked))
                    },
                    colors = switchColors
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            ItirafButton(
                text = stringResource(R.string.register),
                isLoading = state.isLoading,

                enabled = state.email.isNotBlank() &&
                        state.password.isNotBlank() &&
                        state.isPrivacyAccepted &&
                        state.isTermsAccepted,

                onClick = {
                    focusManager.clearFocus()
                    onRegisterClick()
                }
            )

            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.register_already_account),
                    color = ItirafTheme.colors.textSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.login),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable { onLoginClick() }
                )
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
fun RegisterScreenPreview() {
    ItirafAppTheme {
        RegisterContent(
            state = RegisterState(),
            onEvent = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}