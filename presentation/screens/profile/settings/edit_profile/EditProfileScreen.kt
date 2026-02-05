package com.itirafapp.android.presentation.screens.profile.settings.edit_profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is EditProfileUiEvent.NavigateToLogin -> {
                    onDeleteClick()
                }

                is EditProfileUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    EditProfileContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    state: EditProfileState,
    onEvent: (EditProfileEvent) -> Unit,
    onBackClick: () -> Unit
) {
    if (state.showDeleteDialog) {
        GenericAlertDialog(
            onDismissRequest = { onEvent(EditProfileEvent.DeleteDialogDismissed) },
            title = stringResource(R.string.delete_account),
            text = stringResource(R.string.profile_delete_account_confirmation_message),
            confirmButtonText = stringResource(R.string.delete),
            onConfirmClick = { onEvent(EditProfileEvent.DeleteConfirmed) },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissClick = { onEvent(EditProfileEvent.DeleteDialogDismissed) },
            isDestructive = true
        )
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.edit_profile),
                canNavigateBack = true,
                onNavigateBack = { onBackClick() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.username),
                style = MaterialTheme.typography.titleMedium,
                color = ItirafTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.username,
                onValueChange = {},
                enabled = false,
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = ItirafTheme.colors.backgroundCard,
                    disabledTextColor = ItirafTheme.colors.textTertiary,
                    disabledBorderColor = Color.LightGray.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.email),
                style = MaterialTheme.typography.titleMedium,
                color = ItirafTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = {},
                enabled = false,
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = ItirafTheme.colors.backgroundCard,
                    disabledTextColor = ItirafTheme.colors.textTertiary,
                    disabledBorderColor = Color.LightGray.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = ItirafTheme.colors.dividerColor
            )

            Spacer(modifier = Modifier.weight(1f))

            ItirafButton(
                text = stringResource(R.string.delete_account),
                onClick = { onEvent(EditProfileEvent.DeleteIconClicked) },
                containerColor = ItirafTheme.colors.statusError,
                contentColor = ItirafTheme.colors.textPrimary,
                isLoading = state.isLoading,
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
