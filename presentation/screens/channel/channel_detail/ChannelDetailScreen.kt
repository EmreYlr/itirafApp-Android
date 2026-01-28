package com.itirafapp.android.presentation.screens.channel.channel_detail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.presentation.components.core.ConfessionCard
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.channel.components.ChannelDetailHeader
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ChannelDetailScreen(
    onBackClick: () -> Unit,
    onConfessionClick: (String) -> Unit,
    viewModel: ChannelDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ChannelDetailUiEvent.NavigateBack -> {
                    onBackClick()
                }

                is ChannelDetailUiEvent.NavigateToConfessionDetail -> {
                    onConfessionClick(event.id.toString())
                }

                is ChannelDetailUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ChannelDetailContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelDetailContent(
    state: ChannelDetailState,
    onEvent: (ChannelDetailEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = state.channelTitle,
                canNavigateBack = true,
                onNavigateBack = { onBackClick() }
            )
        }
    ) { paddingValues ->

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(ChannelDetailEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = rememberPullToRefreshState(),
                    isRefreshing = state.isRefreshing,
                    containerColor = ItirafTheme.colors.backgroundCard,
                    color = ItirafTheme.colors.brandPrimary,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                item {
                    ChannelDetailHeader(
                        title = state.channelTitle,
                        isFollowed = state.isFollowing,
                        onFollowClick = { onEvent(ChannelDetailEvent.ToggleFollow) }
                    )
                }

                if (state.confessions.isEmpty() && !state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Bu kanalda henüz itiraf yok.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ItirafTheme.colors.textSecondary
                            )
                        }
                    }
                }

                itemsIndexed(state.confessions) { index, confession ->
                    if (index >= state.confessions.lastIndex && !state.isLoading) {
                        onEvent(ChannelDetailEvent.LoadMore)
                    }

                    ConfessionCard(
                        confession = confession,
                        onLikeClick = { onEvent(ChannelDetailEvent.LikeConfession(confession.id)) },
                        onCommentClick = { onEvent(ChannelDetailEvent.ConfessionClicked(confession.id)) },
                        onCardClick = { onEvent(ChannelDetailEvent.ConfessionClicked(confession.id)) }
                    )

                    if (index < state.confessions.lastIndex) {
                        HorizontalDivider(
                            color = ItirafTheme.colors.dividerColor,
                            thickness = 0.5.dp
                        )
                    }
                }

                if (state.isLoading && !state.isRefreshing && state.confessions.isNotEmpty()) {
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

            if (state.isLoading && !state.isRefreshing && state.confessions.isEmpty()) {
                CircularProgressIndicator(
                    color = ItirafTheme.colors.brandPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}