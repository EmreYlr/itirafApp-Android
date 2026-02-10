package com.itirafapp.android.presentation.screens.message.direct_message

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.itirafapp.android.presentation.components.core.EmptyStateView
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.screens.message.components.DirectMessageRow
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun DirectMessageScreen(
    onChatClick: (String, String) -> Unit,
    viewModel: DirectMessageViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val localContext = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is DirectMessageUiEvent.NavigateToDetail -> {
                    onChatClick(event.id, event.title)
                }

                is DirectMessageUiEvent.ShowMessage -> {
                    Toast.makeText(
                        localContext,
                        event.message.asString(localContext),
                        Toast.LENGTH_SHORT
                    ).show()
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (state.directMessages.isNotEmpty()) {
                    items(
                        items = state.directMessages,
                        key = { it.roomId }
                    ) { item ->
                        DirectMessageRow(
                            item = item,
                            onClick = {
                                onEvent(
                                    DirectMessageEvent.DirectMessageClicked(
                                        item.roomId,
                                        item.username
                                    )
                                )
                            },
                            onLongClick = {
                                onEvent(DirectMessageEvent.OnLongClick(item.roomId))
                            }
                        )
                    }
                } else if (!state.isLoading && state.error == null) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center,

                        ) {
                            EmptyStateView(
                                icon = Icons.Default.Forum,
                                message = stringResource(R.string.empty_noMessages_title)
                            )
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(color = ItirafTheme.colors.brandPrimary)
        }
    }
}