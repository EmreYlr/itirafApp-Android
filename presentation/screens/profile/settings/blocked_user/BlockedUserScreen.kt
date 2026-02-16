package com.itirafapp.android.presentation.screens.profile.settings.blocked_user

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.EmptyStateView
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.profile.settings.blocked_user.components.BlockedUserRow
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BlockedUserScreen(
    onBackClick: () -> Unit,
    viewModel: BlockedUserViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is BlockedUserUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    BlockedUserContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedUserContent(
    state: BlockedUserState,
    onEvent: (BlockedUserEvent) -> Unit,
    onBackClick: () -> Unit
) {
    if (state.showUnblockDialog) {
        GenericAlertDialog(
            onDismissRequest = { onEvent(BlockedUserEvent.UnblockDialogDismissed) },
            title = stringResource(R.string.unblock_user),
            text = stringResource(R.string.unblock_confirmation_message),
            confirmButtonText = stringResource(R.string.yes),
            onConfirmClick = { onEvent(BlockedUserEvent.UnblockConfirmed) },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissClick = { onEvent(BlockedUserEvent.UnblockDialogDismissed) },
            isDestructive = true
        )
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.blocked_users),
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
                onRefresh = { onEvent(BlockedUserEvent.Refresh) },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.blocked_users_description),
                                style = MaterialTheme.typography.bodyMedium,
                                color = ItirafTheme.colors.textSecondary
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            if (state.blockedUsers.isNotEmpty()) {
                                Text(
                                    text = stringResource(
                                        R.string.blocked_users_count,
                                        state.blockedUsers.size
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = ItirafTheme.colors.brandPrimary
                                )
                            }
                        }
                    }

                    if (state.blockedUsers.isNotEmpty()) {
                        items(
                            items = state.blockedUsers,
                            key = { it.userId }
                        ) { user ->
                            BlockedUserRow(
                                item = user,
                                onUnblockClick = {
                                    onEvent(BlockedUserEvent.UnblockButtonClicked(user.userId))
                                }
                            )
                        }
                    } else if (!state.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 50.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                EmptyStateView(
                                    icon = Icons.Default.PersonOff,
                                    message = stringResource(R.string.empty_noBlockedUser_title)
                                )
                            }
                        }
                    }
                }
            }

            if (state.isLoading && !state.isRefreshing) {
                CircularProgressIndicator(
                    color = ItirafTheme.colors.brandPrimary
                )
            }
        }
    }
}
