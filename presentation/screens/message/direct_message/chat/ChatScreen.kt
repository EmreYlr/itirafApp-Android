package com.itirafapp.android.presentation.screens.message.direct_message.chat

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.model.ChatUiItem
import com.itirafapp.android.presentation.screens.message.components.ChatActions
import com.itirafapp.android.presentation.screens.message.components.ChatBubble
import com.itirafapp.android.presentation.screens.message.components.ChatInputBar
import com.itirafapp.android.presentation.screens.message.components.DateSeparator
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    onOpenReport: (ReportTarget) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    val listState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is ChatUiEvent.NavigateToBack -> {
                    onBackClick()
                }

                is ChatUiEvent.OpenReportSheet -> {
                    onOpenReport(uiEvent.target)
                }

                is ChatUiEvent.ScrollToBottom -> {
                    listState.animateScrollToItem(0)
                }

                is ChatUiEvent.ShowMessage -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    ChatContent(
        state = state,
        listState = listState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun ChatContent(
    state: ChatState,
    listState: LazyListState,
    onEvent: (ChatEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    if (state.showBlockDialog) {
        GenericAlertDialog(
            onDismissRequest = { onEvent(ChatEvent.DismissBlockDialog) },
            title = stringResource(R.string.block_user_title),
            text = stringResource(R.string.block_user_description),
            confirmButtonText = stringResource(R.string.block),
            isDestructive = true,
            onConfirmClick = {
                onEvent(ChatEvent.BlockUserConfirmed)
            },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissClick = { onEvent(ChatEvent.DismissBlockDialog) }
        )
    }

    val isScrolledToEnd by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 3
        }
    }

    LaunchedEffect(isScrolledToEnd, state.isLoadingMore, state.isLoading, state.hasNextPage) {
        if (isScrolledToEnd && !state.isLoadingMore && !state.isLoading && state.hasNextPage) {
            onEvent(ChatEvent.LoadMoreMessage)
        }
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = state.roomName,
                canNavigateBack = true,
                onNavigateBack = { onBackClick() },
                actions = {
                    ChatActions(
                        onBlockUserClick = { onEvent(ChatEvent.BlockUserClicked) },
                        onReportUserClick = { onEvent(ChatEvent.ReportUserClicked) },
                    )
                }
            )
        },
        bottomBar = {
            ChatInputBar(
                text = state.messageInput,
                onTextChange = { onEvent(ChatEvent.MessageInputChanged(it)) },
                onSendClick = { onEvent(ChatEvent.SendMessage) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(state.messages) { uiItem ->
                when (uiItem) {
                    is ChatUiItem.MessageItem -> ChatBubble(item = uiItem)
                    is ChatUiItem.DateSeparator -> DateSeparator(dateText = uiItem.dateText)
                }
            }

            if (state.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = ItirafTheme.colors.brandPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ItirafTheme.colors.brandPrimary)
            }
        }
    }
}