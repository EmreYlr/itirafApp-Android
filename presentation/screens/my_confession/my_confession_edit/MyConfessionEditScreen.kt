package com.itirafapp.android.presentation.screens.my_confession.my_confession_edit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.enums.ConfessionDisplayStatus
import com.itirafapp.android.presentation.components.content.StatusBadge
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.displayStatus
import com.itirafapp.android.util.extension.getLabel

@Composable
fun MyConfessionEditConfessionScreen(
    data: MyConfessionData,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: MyConfessionEditViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(data) {
        viewModel.setInitialData(data)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MyConfessionEditUiEvent.NavigateToBack -> {
                    onBackClick()
                }

                is MyConfessionEditUiEvent.NavigateToRoot -> {
                    onSuccess()
                }

                is MyConfessionEditUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    MyConfessionEditContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyConfessionEditContent(
    state: MyConfessionEditState,
    onEvent: (MyConfessionEditEvent) -> Unit
) {
    val maxTitleLength = 50
    val maxMessageLength = 1000
    val context = LocalContext.current

    if (state.showDeleteDialog) {
        GenericAlertDialog(
            onDismissRequest = { onEvent(MyConfessionEditEvent.DeleteDialogDismissed) },
            title = stringResource(R.string.delete_confession_title),
            text = stringResource(R.string.delete_description),
            confirmButtonText = stringResource(R.string.delete),
            onConfirmClick = { onEvent(MyConfessionEditEvent.DeleteConfirmed) },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissClick = { onEvent(MyConfessionEditEvent.DeleteDialogDismissed) },
            isDestructive = true
        )
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            TopBar(
                title = stringResource(R.string.edit),
                canNavigateBack = true,
                onNavigateBack = { onEvent(MyConfessionEditEvent.BackClicked) },
                actions = {
                    if (state.confessions != null) {
                        IconButton(
                            onClick = {
                                onEvent(MyConfessionEditEvent.DeleteIconClicked)
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            state.confessions?.let { confession ->
                val (statusText, statusColor) = when (confession.displayStatus) {
                    ConfessionDisplayStatus.APPROVED -> Pair(
                        stringResource(R.string.confession_status_active),
                        ItirafTheme.colors.statusSuccess
                    )

                    ConfessionDisplayStatus.REJECTED -> Pair(
                        stringResource(R.string.confession_status_rejected),
                        ItirafTheme.colors.statusError
                    )

                    ConfessionDisplayStatus.IN_REVIEW -> Pair(
                        stringResource(R.string.confession_status_pending),
                        ItirafTheme.colors.statusPending
                    )

                    else -> Pair(
                        stringResource(R.string.confession_status_unknown),
                        ItirafTheme.colors.textSecondary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.status_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ItirafTheme.colors.textPrimary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    StatusBadge(text = statusText, color = statusColor)

                    if (confession.isNsfw) {
                        Spacer(modifier = Modifier.width(8.dp))
                        StatusBadge(
                            text = stringResource(R.string.confession_nsfw),
                            color = ItirafTheme.colors.statusError
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (confession.displayStatus == ConfessionDisplayStatus.REJECTED &&
                    (!confession.rejectionReason.isNullOrBlank() || !confession.violations.isNullOrEmpty())
                ) {

                    val violationsText = confession.violations?.joinToString(", ") { violation ->
                        violation.getLabel(context)
                    } ?: ""

                    val reasonText = buildString {
                        append(
                            confession.rejectionReason
                                ?: stringResource(R.string.confession_status_unknown)
                        )
                        if (violationsText.isNotEmpty()) {
                            append(" ($violationsText)")
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = ItirafTheme.colors.statusError.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = ItirafTheme.colors.statusError,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = reasonText,
                            style = MaterialTheme.typography.bodySmall,
                            color = ItirafTheme.colors.textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.post_title),
                        style = MaterialTheme.typography.labelLarge,
                        color = ItirafTheme.colors.textPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = "(${stringResource(R.string.post_options)})",
                        style = MaterialTheme.typography.bodySmall,
                        color = ItirafTheme.colors.textTertiary,
                        fontSize = 10.sp
                    )
                }

                Text(
                    text = "${state.title?.length ?: 0}/$maxTitleLength",
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textSecondary,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.title ?: "",
                onValueChange = {
                    if (it.length <= maxTitleLength)
                        onEvent(MyConfessionEditEvent.TitleTextChanged(it))
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.post_title_placeholder),
                        color = ItirafTheme.colors.textSecondary.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ItirafTheme.colors.brandPrimary,
                    unfocusedBorderColor = ItirafTheme.colors.dividerColor,
                    cursorColor = ItirafTheme.colors.brandPrimary,
                    focusedTextColor = ItirafTheme.colors.textPrimary,
                    unfocusedTextColor = ItirafTheme.colors.textPrimary
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.post_description),
                    style = MaterialTheme.typography.labelLarge,
                    color = ItirafTheme.colors.textPrimary,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "${state.message.length}/$maxMessageLength",
                    style = MaterialTheme.typography.bodySmall,
                    color = ItirafTheme.colors.textSecondary,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.message,
                onValueChange = {
                    if (it.length <= maxMessageLength)
                        onEvent(MyConfessionEditEvent.MessageTextChanged(it))
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.post_description_placeholder),
                        color = ItirafTheme.colors.textSecondary.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ItirafTheme.colors.brandPrimary,
                    unfocusedBorderColor = ItirafTheme.colors.dividerColor,
                    cursorColor = ItirafTheme.colors.brandPrimary,
                    focusedTextColor = ItirafTheme.colors.textPrimary,
                    unfocusedTextColor = ItirafTheme.colors.textPrimary
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            val isFormValid = state.message.isNotBlank() && !state.isLoading

            ItirafButton(
                text = stringResource(R.string.edit),
                onClick = { onEvent(MyConfessionEditEvent.SendClicked) },
                isLoading = state.isLoading,
                enabled = isFormValid,
                containerColor = ItirafTheme.colors.brandPrimary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ItirafButton(
                text = stringResource(R.string.cancel),
                onClick = { onEvent(MyConfessionEditEvent.BackClicked) },
                isLoading = false,
                enabled = true,
                containerColor = ItirafTheme.colors.backgroundCard,
                contentColor = ItirafTheme.colors.textPrimary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}