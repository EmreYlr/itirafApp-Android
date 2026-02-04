package com.itirafapp.android.presentation.screens.home.notification

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.home.components.NotificationItemRow
import com.itirafapp.android.presentation.screens.home.components.NotificationSelectionTopBar
import com.itirafapp.android.presentation.screens.home.components.SectionHeader
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun NotificationScreen(
    onBackClick: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is NotificationUiEvent.NavigateToBack -> {
                    onBackClick()
                }

                is NotificationUiEvent.NavigateToDetail -> {
                    //TODO: Route
                }

                is NotificationUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    NotificationContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotificationContent(
    state: NotificationState,
    onEvent: (NotificationEvent) -> Unit,
    onBackClick: () -> Unit
) {
    BackHandler(enabled = state.isSelectionMode) {
        onEvent(NotificationEvent.ClearSelection)
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            if (state.isSelectionMode) {
                NotificationSelectionTopBar(
                    selectedCount = state.selectedIds.size,
                    onCancelClick = { onEvent(NotificationEvent.ClearSelection) },
                    onDeleteClick = { onEvent(NotificationEvent.DeleteSelected) }
                )
            } else {
                TopBar(
                    title = stringResource(R.string.notifications),
                    canNavigateBack = true,
                    onNavigateBack = { onBackClick() },
                    actions = {
                        if (state.notifications.isNotEmpty()) {
                            TextButton(onClick = { onEvent(NotificationEvent.DeleteAllNotifications) }) {
                                Text(
                                    text = stringResource(R.string.delete_all),
                                    color = ItirafTheme.colors.actionLike,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(NotificationEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (state.unreadList.isNotEmpty()) {
                    stickyHeader {
                        SectionHeader(
                            title = stringResource(R.string.notification_new),
                            isButtonVisible = true,
                            onButtonClick = { onEvent(NotificationEvent.MarkAllAsSeen) }
                        )
                    }

                    items(
                        items = state.unreadList,
                        key = { it.id }
                    ) { item ->
                        NotificationItemRow(
                            item = item,
                            isSelectionMode = state.isSelectionMode,
                            isSelected = state.selectedIds.contains(item.id),
                            onClick = {
                                if (state.isSelectionMode) {
                                    onEvent(NotificationEvent.ToggleSelection(item.id))
                                } else {
                                    onEvent(NotificationEvent.OnNotificationClick(item))
                                }
                            },
                            onLongClick = {
                                onEvent(NotificationEvent.ToggleSelection(item.id))
                            },
                            modifier = Modifier.animateItem()
                        )
                    }
                }

                if (state.readList.isNotEmpty()) {
                    stickyHeader {
                        SectionHeader(
                            title = stringResource(R.string.notification_old),
                            isButtonVisible = false
                        )
                    }

                    items(
                        items = state.readList,
                        key = { it.id }
                    ) { item ->
                        NotificationItemRow(
                            item = item,
                            isSelectionMode = state.isSelectionMode,
                            isSelected = state.selectedIds.contains(item.id),
                            onClick = {
                                if (state.isSelectionMode) {
                                    onEvent(NotificationEvent.ToggleSelection(item.id))
                                } else {
                                    onEvent(NotificationEvent.OnNotificationClick(item))
                                }
                            },
                            onLongClick = {
                                onEvent(NotificationEvent.ToggleSelection(item.id))
                            },
                            modifier = Modifier.animateItem()
                        )
                    }
                }

                if (state.isLoadingMore) {
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

                if (!state.isLoading && state.notifications.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(bottom = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsOff,
                                    contentDescription = null,
                                    tint = ItirafTheme.colors.textTertiary,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Henüz bildirim yok",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = ItirafTheme.colors.textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ItirafTheme.colors.brandPrimary)
        }
    }
}