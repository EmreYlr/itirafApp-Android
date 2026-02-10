package com.itirafapp.android.presentation.screens.message.inbox

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.InboxMessage
import com.itirafapp.android.presentation.components.core.EmptyStateView
import com.itirafapp.android.presentation.screens.message.components.InboxRow
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun InboxScreen(
    onDetailClick: (InboxMessage) -> Unit,
    viewModel: InboxViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val localContext = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is InboxUiEvent.NavigateToDetail -> {
                    onDetailClick(event.data)
                }

                is InboxUiEvent.ShowMessage -> {
                    Toast.makeText(
                        localContext,
                        event.message.asString(localContext),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    InboxContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxContent(
    state: InboxState,
    onEvent: (InboxEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(InboxEvent.Refresh) },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (state.inboxMessage.isNotEmpty()) {
                    items(
                        items = state.inboxMessage,
                        key = { it.requestId }
                    ) { message ->
                        InboxRow(
                            item = message,
                            onRowClick = {
                                onEvent(InboxEvent.InboxClicked(message.requestId))
                            },
                            onApproveClick = {
                                onEvent(InboxEvent.ApproveClicked(message.requestId))
                            },
                            onRejectClick = {
                                onEvent(InboxEvent.RejectClicked(message.requestId))
                            }
                        )
                    }
                } else if (!state.isLoading && state.error == null) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyStateView(
                                icon = Icons.Default.MailOutline,
                                message = stringResource(R.string.empty_noRequestMessages_title)
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