package com.itirafapp.android.presentation.screens.message.sent

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.message.components.SentMessageRow
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SentMessageScreen(
    onBackClick: () -> Unit,
    onDetailClick: (SentMessage) -> Unit,
    viewModel: SentMessageViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val localContext = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SentMessageUiEvent.NavigateToDetail -> {
                    onDetailClick(event.data)
                }

                is SentMessageUiEvent.ShowMessage -> {
                    Toast.makeText(
                        localContext,
                        event.message.asString(localContext),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    SentMessageContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentMessageContent(
    state: SentMessageState,
    onEvent: (SentMessageEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.sent_message_title),
                canNavigateBack = true,
                onNavigateBack = { onBackClick() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onEvent(SentMessageEvent.Refresh) },
                modifier = Modifier.fillMaxSize(),

                ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.sentMessage.isNotEmpty()) {
                        items(
                            items = state.sentMessage,
                            key = { it.requestId }
                        ) { item ->
                            SentMessageRow(
                                item = item,
                                onClick = {
                                    onEvent(SentMessageEvent.ItemClicked(item.requestId))
                                }
                            )
                        }
                    } else if (!state.isLoading && state.error == null) {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = null,
                                        tint = ItirafTheme.colors.textTertiary,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Henüz gönderilmiş bir isteğin yok",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = ItirafTheme.colors.textSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(color = ItirafTheme.colors.brandPrimary)
            }

            if (state.error != null && !state.isLoading && state.sentMessage.isEmpty()) {
                Text(
                    text = state.error.asString(),
                    color = ItirafTheme.colors.statusError,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}