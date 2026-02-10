package com.itirafapp.android.presentation.screens.channel

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import com.itirafapp.android.presentation.components.core.ChannelCard
import com.itirafapp.android.presentation.components.core.EmptyStateView
import com.itirafapp.android.presentation.components.core.SearchComponent
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ChannelScreen(
    onChannelClick: (Int, String) -> Unit,
    viewModel: ChannelViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ChannelUiEvent.NavigateToDetail -> {
                    onChannelClick(event.id, event.title)
                }

                is ChannelUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    ChannelContent(
        state = state,
        onEvent = viewModel::onEvent,
        onChannelClick = onChannelClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelContent(
    state: ChannelState,
    onEvent: (ChannelEvent) -> Unit,
    onChannelClick: (Int, String) -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.channel),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchComponent(
                query = state.searchQuery,
                onQueryChanged = { query ->
                    onEvent(ChannelEvent.SearchQueryChanged(query))
                },
                placeholderText = "${stringResource(R.string.channel_search_placeholder)}...",
                onSearchTriggered = {
                    onEvent(ChannelEvent.SearchTriggered)
                }
            )

            HorizontalDivider(color = ItirafTheme.colors.dividerColor)

            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onEvent(ChannelEvent.Refresh) },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                ) {
                    if (state.channel.isEmpty() && !state.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 50.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                EmptyStateView(
                                    icon = Icons.Default.SearchOff,
                                    message = stringResource(R.string.empty_noChannels_title)
                                )
                            }
                        }
                    }
                    itemsIndexed(state.channel) { index, channel ->
                        if (index >= state.channel.lastIndex && !state.isLoading && state.searchQuery.isEmpty()) {
                            onEvent(ChannelEvent.LoadMore)
                        }

                        ChannelCard(
                            channel = channel,
                            isFollowed = channel.isFollowing,
                            onFollowClick = { onEvent(ChannelEvent.FollowClicked(channel.id)) },
                            onClick = { onChannelClick(channel.id, channel.title) }
                        )
                    }

                    if (state.isLoading && !state.isRefreshing && state.channel.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = ItirafTheme.colors.brandPrimary,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                if (state.isLoading && !state.isRefreshing && state.channel.isEmpty()) {
                    CircularProgressIndicator(
                        color = ItirafTheme.colors.brandPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}