package com.itirafapp.android.presentation.screens.auth.login

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.components.core.ItirafTextField
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.auth.components.LegalTextMultiLink
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.constant.Constants
import com.itirafapp.android.util.extension.openUrlSafe
import com.itirafapp.android.util.manager.GoogleAuthManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToPasswordReset: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val colorParams = ItirafTheme.colors.brandPrimary.toArgb()
    val scope = rememberCoroutineScope()
    val googleAuthManager = remember { GoogleAuthManager(context) }
    var showResendDialog by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is LoginUiEvent.NavigateToHome -> {
                    onNavigateToHome()
                }

                is LoginUiEvent.NavigateToRegister -> {
                    onNavigateToRegister()
                }

                is LoginUiEvent.NavigateToForgotPassword -> {
                    onNavigateToPasswordReset()
                }

                is LoginUiEvent.ShowPrivacyPolicyDialog -> {
                    openUrlSafe(context, Constants.PRIVACY_URL, colorParams)
                }

                is LoginUiEvent.ShowTermsDialog -> {
                    openUrlSafe(context, Constants.TERMS_URL, colorParams)
                }

                is LoginUiEvent.ShowResendDialog -> {
                    showResendDialog = true
                }

                is LoginUiEvent.HideResendDialog -> {
                    showResendDialog = false
                }

                is LoginUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    if (showResendDialog) {
        GenericAlertDialog(
            onDismissRequest = {
                viewModel.onEvent(LoginEvent.DismissResendDialog)
            },
            title = stringResource(R.string.warning),
            text = stringResource(R.string.message_account_not_verified),

            confirmButtonText = stringResource(R.string.error_send_resend),
            onConfirmClick = {
                viewModel.onEvent(LoginEvent.ResendEmailClicked)
            },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissClick = {
                viewModel.onEvent(LoginEvent.DismissResendDialog)
            }
        )
    }

    LoginContent(
        state = state,
        onEvent = viewModel::onEvent,
        onRegisterClick = { viewModel.onEvent(LoginEvent.RegisterClicked) },
        onForgotPasswordClick = { viewModel.onEvent(LoginEvent.ForgotPasswordClicked) },
        onLoginClick = {
            focusManager.clearFocus()
            viewModel.onEvent(LoginEvent.LoginClicked)
        },
        onAnonLogin = {
            focusManager.clearFocus()
            viewModel.onEvent(LoginEvent.AnonymousLoginClicked)
        },
        onGoogleLoginClick = {
            focusManager.clearFocus()
            scope.launch {
                val token = googleAuthManager.signIn(context)

                if (token != null) {
                    viewModel.onEvent(LoginEvent.OnGoogleLoginSuccess(token))
                } else {
                    viewModel.onEvent(LoginEvent.OnGoogleLoginError)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onAnonLogin: () -> Unit,
    onGoogleLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val isInteractionEnabled = !state.isLoading

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.login)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                ItirafTextField(
                    value = state.email,
                    onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
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
                    onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
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

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(
                        onClick = { if (isInteractionEnabled) onForgotPasswordClick() },
                        enabled = isInteractionEnabled,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.forgot_password),
                            color = if (isInteractionEnabled) ItirafTheme.colors.textSecondary else ItirafTheme.colors.textSecondary.copy(
                                alpha = 0.5f
                            ),
                            fontSize = 14.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ItirafButton(
                    text = stringResource(R.string.login_button),
                    onClick = {
                        focusManager.clearFocus()
                        onLoginClick()
                    },
                    isLoading = false,
                    enabled = isInteractionEnabled
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = ItirafTheme.colors.dividerColor,
                        thickness = 1.dp
                    )
                    Text(
                        text = stringResource(R.string.or),
                        color = ItirafTheme.colors.textSecondary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = ItirafTheme.colors.dividerColor,
                        thickness = 1.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ItirafButton(
                    text = stringResource(R.string.apple_login_button),
                    onClick = {
                        focusManager.clearFocus()
                        //TODO: onAppleLogin()
                    },
                    icon = ImageVector.vectorResource(id = R.drawable.ic_apple_logo),
                    isLoading = false,
                    enabled = isInteractionEnabled,
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    borderColor = ItirafTheme.colors.textSecondary,
                    borderWidth = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                ItirafButton(
                    text = stringResource(R.string.google_login_button),
                    onClick = {
                        focusManager.clearFocus()
                        onGoogleLoginClick()
                    },
                    icon = ImageVector.vectorResource(id = R.drawable.ic_google_logo),
                    contentColor = ItirafTheme.colors.textPrimary,
                    isLoading = false,
                    enabled = isInteractionEnabled,
                    containerColor = ItirafTheme.colors.backgroundCard,
                    iconTint = Color.Unspecified,
                    borderColor = ItirafTheme.colors.textSecondary,
                    borderWidth = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                ItirafButton(
                    text = stringResource(R.string.anonymous_login_button),
                    onClick = {
                        focusManager.clearFocus()
                        onAnonLogin()
                    },
                    icon = Icons.Default.PersonOff,
                    isLoading = false,
                    enabled = isInteractionEnabled,
                    containerColor = ItirafTheme.colors.textSecondary,
                    borderColor = ItirafTheme.colors.textSecondary,
                    borderWidth = 0.7.dp
                )

                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.login_dont_account),
                        color = ItirafTheme.colors.textSecondary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(R.string.register),
                        color = if (isInteractionEnabled) MaterialTheme.colorScheme.primary else Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable(enabled = isInteractionEnabled) { onRegisterClick() }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                LegalTextMultiLink(
                    onTermsClick = {
                        if (isInteractionEnabled) onEvent(LoginEvent.OpenTermsOfUse)
                    },
                    onPrivacyClick = {
                        if (isInteractionEnabled) onEvent(LoginEvent.OpenPrivacyPolicy)
                    },
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = ItirafTheme.colors.brandPrimary
                    )
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
fun LoginScreenPreview() {
    ItirafAppTheme {
        LoginContent(
            state = LoginState(),
            onEvent = {},
            onRegisterClick = {},
            onLoginClick = {},
            onAnonLogin = {},
            onGoogleLoginClick = {}
        )
    }
}