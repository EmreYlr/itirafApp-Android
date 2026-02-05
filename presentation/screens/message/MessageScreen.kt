package com.itirafapp.android.presentation.screens.message

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Outbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.SegmentedControl
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.message.direct_message.DirectMessageScreen
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import kotlinx.coroutines.launch

@Composable
fun MessageScreen(
    onSentMessageClick: () -> Unit,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MessageUiEvent.NavigateToSentMessage -> {
                    onSentMessageClick()
                }
            }
        }
    }

    MessageContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageContent(
    state: MessageState,
    onEvent: (MessageEvent) -> Unit
) {
    val titles =
        listOf(
            stringResource(R.string.direct_message_title),
            stringResource(R.string.inbox_title)
        )

    val pagerState = rememberPagerState(pageCount = { titles.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onEvent(MessageEvent.TabChanged(pagerState.currentPage))
    }

    LaunchedEffect(state.selectedTabIndex) {
        if (pagerState.currentPage != state.selectedTabIndex && !pagerState.isScrollInProgress) {
            pagerState.animateScrollToPage(state.selectedTabIndex)
        }
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.message_title),
                actions = {
                    IconButton(onClick = {
                        onEvent(MessageEvent.SentMessageClicked)
                    }) {
                        Icon(
                            Icons.Default.Outbox,
                            contentDescription = "Sent Message",
                            tint = ItirafTheme.colors.brandPrimary
                        )
                    }
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
                    onEvent(MessageEvent.TabChanged(newIndex))
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
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> {
                        DirectMessageScreen()
                    }

                    1 -> {
                        //InboxScreen()
                    }
                }
            }
        }
    }
}
