package com.itirafapp.android.presentation.screens.auth.login

import android.widget.Toast
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.common.ItirafButton
import com.itirafapp.android.presentation.components.common.ItirafTextField
import com.itirafapp.android.presentation.components.common.ItirafTopBar
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is LoginUiEvent.NavigateToHome -> {
                    onNavigateToHome()
                }

                is LoginUiEvent.NavigateToRegister -> {
                    onNavigateToRegister()
                }

                is LoginUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    LoginContent(
        state = state,
        onEvent = viewModel::onEvent,
        onRegisterClick = { viewModel.onEvent(LoginEvent.RegisterClicked) },
        onLoginClick = {
            focusManager.clearFocus()
            viewModel.onEvent(LoginEvent.LoginClicked)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            ItirafTopBar(
                title = stringResource(R.string.login)
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

            Spacer(modifier = Modifier.height(24.dp))

            ItirafTextField(
                value = state.email,
                onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                hint = stringResource(R.string.auth_field_email),
                keyboardType = KeyboardType.Email,
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
                    onClick = { /* TODO: Forgot Password */ },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = ItirafTheme.colors.textSecondary,
                        fontSize = 14.sp
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
                isLoading = state.isLoading
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
                isLoading = state.isLoading,
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
                    //TODO: onGoogleLogin()
                },
                icon = ImageVector.vectorResource(id = R.drawable.ic_google_logo),
                contentColor = ItirafTheme.colors.textPrimary,
                isLoading = state.isLoading,
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
                    //TODO: onAnonLogin()
                },
                icon = Icons.Default.PersonOff,
                isLoading = state.isLoading,
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
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable { onRegisterClick() }
                )
            }
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
fun LoginScreenPreview() {
    ItirafAppTheme {
        LoginContent(
            state = LoginState(),
            onEvent = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}