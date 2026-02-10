package com.itirafapp.android.presentation.screens.profile.follow_channel

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupOff
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
fun FollowChannelScreen(
    onBackClick: () -> Unit,
    onChannelClick: (Int, String) -> Unit,
    onGoToChannel: () -> Unit,
    viewModel: FollowChannelViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is FollowChannelUiEvent.NavigateToDetail -> {
                    onChannelClick(event.id, event.title)
                }

                is FollowChannelUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }

                is FollowChannelUiEvent.NavigateToBack -> {
                    onBackClick()
                }
            }
        }
    }

    FollowChannelContent(
        state = state,
        onEvent = viewModel::onEvent,
        onChannelClick = onChannelClick,
        onBackClick = onBackClick,
        onGoToChannel = onGoToChannel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowChannelContent(
    state: FollowChannelState,
    onEvent: (FollowChannelEvent) -> Unit,
    onChannelClick: (Int, String) -> Unit,
    onBackClick: () -> Unit,
    onGoToChannel: () -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.following_channels),
                canNavigateBack = true,
                onNavigateBack = onBackClick
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
                    onEvent(FollowChannelEvent.SearchQueryChanged(query))
                },
                placeholderText = "${stringResource(R.string.following_channels_search)}...",
            )

            HorizontalDivider(color = ItirafTheme.colors.dividerColor)

            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onEvent(FollowChannelEvent.Refresh) },
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
                                    icon = Icons.Default.GroupOff,
                                    message =
                                        if (state.searchQuery.isNotEmpty()) stringResource(R.string.empty_noSearchChannels_title)
                                        else stringResource(R.string.empty_noFollowingChannels_title),
                                    buttonText = stringResource(R.string.empty_noFollowingChannels_button),
                                    onButtonClick = {
                                        onGoToChannel()
                                    }
                                )
                            }
                        }
                    }
                    items(
                        items = state.channel,
                        key = { it.id }
                    ) { channel ->
                        ChannelCard(
                            channel = channel,
                            isFollowed = channel.isFollowing,
                            onFollowClick = { onEvent(FollowChannelEvent.FollowClicked(channel.id)) },
                            onClick = { onChannelClick(channel.id, channel.title) }
                        )
                    }
                }

                if (state.isLoading && !state.isRefreshing) {
                    CircularProgressIndicator(
                        color = ItirafTheme.colors.brandPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}