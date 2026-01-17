package com.itirafapp.android.presentation.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.common.TopBar
import com.itirafapp.android.presentation.screens.home.components.NotificationIcon
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun HomeScreen(
    onConfessionClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToNotifications -> {
                    onNotificationClick()
                }

                is HomeUiEvent.NavigateToConfessionDetail -> {
                    onConfessionClick("postId")
                }
            }
        }
    }

    HomeContent(
        state = state,
        onEvent = viewModel::onEvent,
        onConfessionClick = { postId ->
            focusManager.clearFocus()
            viewModel.onEvent(HomeEvent.ConfessionClicked)
        },
        onNotificationClick = {
            focusManager.clearFocus()
            viewModel.onEvent(HomeEvent.NotificationClicked)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onConfessionClick: (String) -> Unit,
    onNotificationClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.home),
                actions = {
                    NotificationIcon(
                        hasUnread = state.hasUnread,
                        notificationCount = state.notificationCount,
                        onClick = {
                            focusManager.clearFocus()
                            onNotificationClick()
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun HomeScreenPreview() {
    ItirafAppTheme {
        HomeContent(
            state = HomeState(),
            onEvent = {},
            onConfessionClick = {},
            onNotificationClick = {}
        )
    }
}