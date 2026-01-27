package com.itirafapp.android.presentation.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.SegmentedControl
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.home.components.NotificationIcon
import com.itirafapp.android.presentation.screens.home.feed.FeedScreen
import com.itirafapp.android.presentation.screens.home.following.FollowingScreen
import com.itirafapp.android.presentation.ui.theme.ItirafAppTheme
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onConfessionClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onOpenDM: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToNotifications -> {
                    onNotificationClick()
                }
            }
        }
    }

    HomeContent(
        state = state,
        onEvent = viewModel::onEvent,
        onConfessionClick = onConfessionClick,
        onNotificationClick = {
            viewModel.onEvent(HomeEvent.NotificationClicked)
        },
        onOpenDM = onOpenDM
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onConfessionClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onOpenDM: (Int) -> Unit
) {

    val titles =
        listOf(stringResource(R.string.flow_title), stringResource(R.string.following_title))
    val pagerState = rememberPagerState(pageCount = { titles.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (state.selectedTabIndex != pagerState.currentPage) {
            onEvent(HomeEvent.TabChanged(pagerState.currentPage))
        }
    }

    LaunchedEffect(state.selectedTabIndex) {
        if (pagerState.currentPage != state.selectedTabIndex) {
            pagerState.animateScrollToPage(state.selectedTabIndex)
        }
    }

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
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            SegmentedControl(
                modifier = Modifier.padding(horizontal = 16.dp),
                items = titles,
                selectedIndex = state.selectedTabIndex,
                onIndexChange = { newIndex ->
                    if (newIndex == 1 && !state.isUserAuthenticated) {
                        return@SegmentedControl
                    }

                    onEvent(HomeEvent.TabChanged(newIndex))
                    scope.launch { pagerState.animateScrollToPage(newIndex) }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                userScrollEnabled = state.isUserAuthenticated
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> {
                        FeedScreen(
                            onItemClick = onConfessionClick,
                            onOpenDM = onOpenDM
                        )
                    }

                    1 -> {
                        FollowingScreen(
                            onItemClick = onConfessionClick,
                            onOpenDM = onOpenDM
                        )
                    }
                }
            }
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
            onNotificationClick = {},
            onOpenDM = {}
        )
    }
}