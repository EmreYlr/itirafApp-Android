package com.itirafapp.android.presentation.screens.home.feed

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.presentation.components.core.ConfessionCard
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.state.shareLink

@Composable
fun FeedScreen(
    onItemClick: (String) -> Unit,
    onOpenDM: (Int) -> Unit,
    onChannelClick: (Int, String) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is FeedUiEvent.NavigateToDetail -> onItemClick(event.id.toString())

                is FeedUiEvent.OpenDMSheet -> {
                    onOpenDM(event.targetId)
                }

                is FeedUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is FeedUiEvent.OpenShareSheet -> {
                    shareLink(context, event.link)
                }

                is FeedUiEvent.NavigateToChannel -> {
                    onChannelClick(event.id, event.title)
                }
            }
        }
    }

    FeedContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedContent(
    state: FeedState,
    onEvent: (FeedEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(FeedEvent.Refresh) },
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.confessions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    items(
                        items = state.confessions,
                        key = { it.id }
                    ) { confession ->
                        ConfessionCard(
                            confession = confession,
                            onCardClick = { onEvent(FeedEvent.PostClicked(confession.id)) },
                            onChannelClick = {
                                onEvent(
                                    FeedEvent.ChannelClicked(
                                        confession.channel.id,
                                        confession.channel.title
                                    )
                                )
                            },
                            onLikeClick = { onEvent(FeedEvent.LikeClicked(confession.id)) },
                            onCommentClick = { onEvent(FeedEvent.CommentClicked(confession.id)) },
                            onDMRequestClick = { onEvent(FeedEvent.DMRequestClicked(confession.id)) },
                            onShareClick = { onEvent(FeedEvent.ShareClicked(confession.id)) }
                        )

                        HorizontalDivider(
                            color = ItirafTheme.colors.dividerColor,
                            thickness = 0.5.dp
                        )
                    }

                    item {
                        if (state.isLoading && !state.isRefreshing) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = ItirafTheme.colors.textSecondary,
                                    strokeWidth = 2.dp
                                )
                            }
                        } else {
                            LaunchedEffect(true) {
                                onEvent(FeedEvent.LoadMore)
                            }
                        }
                    }
                }
            } else if (!state.isLoading && state.error == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Henüz itiraf yok.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ItirafTheme.colors.textSecondary
                    )
                }
            }
        }

        if (state.isLoading && !state.isRefreshing && state.confessions.isEmpty()) {
            CircularProgressIndicator(color = ItirafTheme.colors.brandPrimary)
        }
    }
}