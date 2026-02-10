package com.itirafapp.android.presentation.screens.profile.social

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.Link
import com.itirafapp.android.domain.model.enums.SocialPlatform
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SocialScreen(
    data: Link?,
    onNavigateBack: () -> Unit,
    viewModel: SocialViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = data) {
        viewModel.setInitialData(data)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SocialUiEvent.NavigateToBack -> {
                    onNavigateBack()
                }

                is SocialUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    SocialContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialContent(
    state: SocialState,
    onEvent: (SocialEvent) -> Unit,
    onBackClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val platforms = remember(state.usedPlatforms, state.platform) {
        SocialPlatform.getSelectablePlatforms().filter { platform ->
            if (platform == state.platform) return@filter true
            !state.usedPlatforms.contains(platform)
        }
    }

    if (state.showDeleteDialog) {
        GenericAlertDialog(
            onDismissRequest = { onEvent(SocialEvent.DeleteDialogDismissed) },
            title = stringResource(R.string.delete_social_account),
            text = stringResource(R.string.delete_description),
            confirmButtonText = stringResource(R.string.delete),
            onConfirmClick = { onEvent(SocialEvent.DeleteConfirmed) },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissClick = { onEvent(SocialEvent.DeleteDialogDismissed) },
            isDestructive = true
        )
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            val title =
                if (state.isEditMode) stringResource(R.string.edit_account)
                else stringResource(R.string.add_account)
            TopBar(
                title = title,
                canNavigateBack = true,
                onNavigateBack = { onBackClick() },
                actions = {
                    if (state.isEditMode) {
                        IconButton(
                            onClick = {
                                onEvent(SocialEvent.DeleteIconClicked)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = ItirafTheme.colors.statusError
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Platform",
                    style = MaterialTheme.typography.titleSmall,
                    color = ItirafTheme.colors.textPrimary,
                    fontWeight = FontWeight.Medium
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value =
                            state.platform?.displayName
                                ?: stringResource(R.string.select_platform),
                        onValueChange = {},
                        readOnly = true,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ItirafTheme.colors.backgroundCard,
                            unfocusedContainerColor = ItirafTheme.colors.backgroundCard,
                            disabledContainerColor = ItirafTheme.colors.backgroundCard,
                            focusedBorderColor = ItirafTheme.colors.brandPrimary,
                            unfocusedBorderColor = ItirafTheme.colors.dividerColor,
                            focusedTextColor = if (state.platform == null) Color.Gray else ItirafTheme.colors.textPrimary,
                            unfocusedTextColor = if (state.platform == null) Color.Gray else ItirafTheme.colors.textPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable
                            ),

                        )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = ItirafTheme.colors.backgroundCard
                    ) {
                        platforms.forEachIndexed { index, platform ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = platform.iconResId),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = Color.Unspecified
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = platform.displayName,
                                            color = ItirafTheme.colors.textPrimary
                                        )
                                    }
                                },
                                onClick = {
                                    onEvent(SocialEvent.PlatformChanged(platform))
                                    expanded = false
                                }
                            )
                            if (index < platforms.lastIndex) {
                                HorizontalDivider(
                                    color = ItirafTheme.colors.dividerColor,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.account_name),
                    style = MaterialTheme.typography.titleSmall,
                    color = ItirafTheme.colors.textPrimary,
                    fontWeight = FontWeight.Medium
                )

                OutlinedTextField(
                    value = state.username,
                    onValueChange = { onEvent(SocialEvent.UsernameChanged(it)) },
                    placeholder = {
                        Text(
                            text = "@${stringResource(R.string.account_name_placeholder)}",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ItirafTheme.colors.backgroundCard,
                        unfocusedContainerColor = ItirafTheme.colors.backgroundCard,
                        focusedBorderColor = ItirafTheme.colors.brandPrimary,
                        unfocusedBorderColor = ItirafTheme.colors.dividerColor,
                        focusedTextColor = ItirafTheme.colors.textPrimary,
                        unfocusedTextColor = ItirafTheme.colors.textPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 14.sp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.account_link),
                    style = MaterialTheme.typography.titleSmall,
                    color = ItirafTheme.colors.textPrimary,
                    fontWeight = FontWeight.Medium
                )

                OutlinedTextField(
                    value = state.fullLink.ifEmpty { "https://" },
                    onValueChange = {},
                    enabled = false,
                    readOnly = true,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = ItirafTheme.colors.dividerColor.copy(alpha = 0.3f),
                        disabledBorderColor = Color.Transparent,
                        disabledTextColor = ItirafTheme.colors.textSecondary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 14.sp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            ItirafButton(
                text =
                    if (state.isEditMode) stringResource(R.string.update)
                    else stringResource(R.string.add_account),
                onClick = { onEvent(SocialEvent.SaveClicked) },
                isLoading = state.isLoading,
                containerColor = ItirafTheme.colors.brandPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
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
fun SocialScreenPreview() {
    ItirafAppTheme {
        SocialContent(
            state = SocialState(),
            onEvent = {},
            onBackClick = {}
        )
    }
}