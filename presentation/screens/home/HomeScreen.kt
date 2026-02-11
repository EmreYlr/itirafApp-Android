package com.itirafapp.android.presentation.screens.home

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.SegmentedControl
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.home.components.NotificationIcon
import com.itirafapp.android.presentation.screens.home.components.NotificationPermissionEffect
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
    onPostConfessionClick: () -> Unit,
    onChannelClick: (Int, String) -> Unit,
    onGoToChannel: () -> Unit,
    bottomBarPadding: Dp = 0.dp,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state

    NotificationPermissionEffect(
        onResult = { isGranted ->
            viewModel.onNotificationPermissionResult(isGranted)
        }
    )

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
        onOpenDM = onOpenDM,
        onPostConfessionClick = onPostConfessionClick,
        onChannelClick = onChannelClick,
        onGoToChannel = onGoToChannel,
        bottomBarPadding = bottomBarPadding
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onConfessionClick: (String) -> Unit,
    onOpenDM: (Int) -> Unit,
    onPostConfessionClick: () -> Unit,
    onChannelClick: (Int, String) -> Unit,
    onGoToChannel: () -> Unit,
    bottomBarPadding: Dp = 0.dp
) {

    val titles =
        listOf(stringResource(R.string.flow_title), stringResource(R.string.following_title))
    val pagerState = rememberPagerState(pageCount = { titles.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onEvent(HomeEvent.TabChanged(pagerState.currentPage))
    }

    LaunchedEffect(state.selectedTabIndex) {
        if (pagerState.currentPage != state.selectedTabIndex && !pagerState.isScrollInProgress) {
            pagerState.animateScrollToPage(state.selectedTabIndex)
        }
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        floatingActionButton = {
            val fabBackgroundColor = if (state.isUserAuthenticated) {
                ItirafTheme.colors.brandPrimary
            } else {
                Color.Gray
            }

            val iconColor = if (state.isUserAuthenticated) {
                Color.White
            } else {
                Color.White.copy(alpha = 0.5f)
            }

            FloatingActionButton(
                onClick = {
                    if (state.isUserAuthenticated) {
                        onPostConfessionClick()
                    }
                },
                containerColor = fabBackgroundColor,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = bottomBarPadding)
            ) {
                Icon(
                    imageVector = Icons.Default.AddComment,
                    contentDescription = "Add",
                    tint = iconColor
                )
            }
        },
        topBar = {
            TopBar(
                title = stringResource(R.string.home),
                actions = {
                    NotificationIcon(
                        hasUnread = state.hasUnread,
                        notificationCount = state.notificationCount,
                        onClick = {
                            onEvent(HomeEvent.NotificationClicked)
                        },
                        isEnabled = state.isUserAuthenticated
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
                    scope.launch {
                        pagerState.animateScrollToPage(
                            page = newIndex,
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
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
                            onOpenDM = onOpenDM,
                            onChannelClick = onChannelClick,
                            onHomeRefresh = {
                                onEvent(HomeEvent.RefreshNotifications)
                            }
                        )
                    }

                    1 -> {
                        FollowingScreen(
                            onItemClick = onConfessionClick,
                            onOpenDM = onOpenDM,
                            onChannelClick = onChannelClick,
                            onHomeRefresh = {
                                onEvent(HomeEvent.RefreshNotifications)
                            },
                            onGoToChannel = onGoToChannel
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
            onOpenDM = {},
            onPostConfessionClick = {},
            onChannelClick = { _, _ -> },
            onGoToChannel = {}
        )
    }
}