package com.itirafapp.android.presentation.screens.message.inbox

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun InboxScreen(
    viewModel: InboxViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val localContext = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is InboxUiEvent.NavigateToDetail -> {

                }

                is InboxUiEvent.ShowMessage -> {
                    Toast.makeText(localContext, event.message, Toast.LENGTH_SHORT).show()

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

        }
    }
}