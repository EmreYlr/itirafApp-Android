package com.itirafapp.android.presentation.screens.message.direct_message

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.screens.message.components.DirectMessageRow
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun DirectMessageScreen(
    viewModel: DirectMessageViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val localContext = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is DirectMessageUiEvent.NavigateToDetail -> {

                }

                is DirectMessageUiEvent.ShowMessage -> {
                    Toast.makeText(localContext, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    DirectMessageContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectMessageContent(
    state: DirectMessageState,
    onEvent: (DirectMessageEvent) -> Unit
) {
    if (state.showDeleteDialog) {
        var isBlocked by remember { mutableStateOf(false) }

        GenericAlertDialog(
            onDismissRequest = { onEvent(DirectMessageEvent.DismissDeleteDialog) },
            title = stringResource(R.string.delete_messages),
            text = stringResource(R.string.delete_messages_description),
            confirmButtonText = stringResource(R.string.delete),
            isDestructive = true,
            onConfirmClick = {
                onEvent(DirectMessageEvent.DeleteRoom(isBlocked))
            },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissClick = { onEvent(DirectMessageEvent.DismissDeleteDialog) },
            checkboxText = stringResource(R.string.block_user_title),
            isCheckboxChecked = isBlocked,
            onCheckboxCheckedChange = { isBlocked = it }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(DirectMessageEvent.Refresh) },
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.directMessages.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = state.directMessages,
                        key = { it.roomId }
                    ) { item ->
                        DirectMessageRow(
                            item = item,
                            onClick = {
                                onEvent(DirectMessageEvent.DirectMessageClicked(item.roomId))
                            },
                            onLongClick = {
                                onEvent(DirectMessageEvent.OnLongClick(item.roomId))
                            }
                        )
                    }
                }
            } else if (!state.isLoading && state.error.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = null,
                            tint = ItirafTheme.colors.textTertiary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Henüz mesajınız yok",
                            style = MaterialTheme.typography.bodyLarge,
                            color = ItirafTheme.colors.textSecondary
                        )
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(color = ItirafTheme.colors.brandPrimary)
        }

        if (state.error.isNotEmpty() && !state.isLoading) {
            Text(
                text = state.error,
                color = ItirafTheme.colors.statusError,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}