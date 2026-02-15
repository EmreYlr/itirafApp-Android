package com.itirafapp.android.presentation.screens.moderation

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.domain.model.enums.ModerationFilter
import com.itirafapp.android.presentation.components.core.EmptyStateView
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.moderation.components.ModerationCard
import com.itirafapp.android.presentation.screens.moderation.components.ModerationSegmentedControl
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationScreen(
    onBackClick: () -> Unit,
    onItemClick: (ModerationData) -> Unit,
    viewModel: ModerationViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(ModerationEvent.LoadData)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ModerationUiEvent.NavigateToDetail -> {
                    onItemClick(event.data)
                }

                is ModerationUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    ModerationContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModerationContent(
    state: ModerationState,
    onEvent: (ModerationEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.moderation),
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                ModerationSegmentedControl(
                    selectedFilter = state.selectedFilter,
                    onFilterSelected = { newFilter ->
                        onEvent(ModerationEvent.FilterChanged(newFilter))
                    }
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { onEvent(ModerationEvent.Refresh) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            top = 4.dp
                        )
                    ) {
                        if (state.moderationData.isNotEmpty()) {
                            itemsIndexed(state.moderationData) { index, item ->
                                if (index >= state.moderationData.lastIndex && !state.isLoading && !state.isRefreshing) {
                                    onEvent(ModerationEvent.LoadMore)
                                }

                                Box(modifier = Modifier.clickable {
                                    onEvent(ModerationEvent.ItemClicked(item.id))
                                }) {
                                    ModerationCard(
                                        data = item,
                                        onApproveClick = { onEvent(ModerationEvent.ItemClicked(item.id)) },
                                        onRejectClick = { onEvent(ModerationEvent.ItemClicked(item.id)) }
                                    )
                                }
                            }

                            if (state.isLoading) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
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
                        } else if (!state.isLoading && state.error == null) {
                            item {
                                Box(modifier = Modifier.fillParentMaxSize()) {
                                    EmptyStateView(
                                        icon = Icons.Outlined.Inbox,
                                        message = when (state.selectedFilter) {
                                            ModerationFilter.ALL -> stringResource(R.string.moderation_empty_all)
                                            ModerationFilter.PENDING -> stringResource(R.string.moderation_empty_pending)
                                            ModerationFilter.REJECTED -> stringResource(R.string.moderation_empty_rejected)
                                        },
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }

                if (state.isLoading && state.moderationData.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ItirafTheme.colors.brandPrimary
                    )
                }
            }
        }

    }
}