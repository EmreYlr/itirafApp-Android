package com.itirafapp.android.presentation.screens.auth.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.common.ItirafTextField
import com.itirafapp.android.presentation.components.common.ItirafTopBar
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is RegisterUiEvent.NavigateToLogin -> {
                    onNavigateBack()
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
            ItirafTopBar(
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
                "E-Posta ile kayıt ol",
                style = MaterialTheme.typography.titleLarge,
                color = ItirafTheme.colors.textPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Text(
                "Hesap oluşturarak itiraflarınızı paylaşmaya hemen başlayabilirsiniz.",
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
                imeAction = ImeAction.Next,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Mail,
                        contentDescription = "Email Icon",
                        tint = ItirafTheme.colors.textSecondary
                    )
                }
            )
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