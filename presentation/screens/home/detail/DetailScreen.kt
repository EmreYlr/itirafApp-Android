package com.itirafapp.android.presentation.screens.home.detail

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.presentation.components.core.CommentInputBar
import com.itirafapp.android.presentation.components.core.ReplyCard
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.home.components.DetailHeader
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun DetailScreen(
    onNavigationBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is DetailUiEvent.NavigateToBack -> {
                    onNavigationBack()
                }

                is DetailUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    DetailContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    state: DetailState,
    onEvent: (DetailEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            TopBar(
                canNavigateBack = true,
                onNavigateBack = { onEvent(DetailEvent.BackClicked) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = ItirafTheme.colors.brandPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.error != null) {
                Text(
                    text = state.error,
                    color = ItirafTheme.colors.statusError,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.confession != null) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        DetailHeader(
                            confessionDetail = state.confession,
                            onLikeClick = { onEvent(DetailEvent.LikeClicked(it)) },
                            onCommentClick = { },
                            onDMRequestClick = { onEvent(DetailEvent.DMRequestClicked(it)) },
                            onShareClick = { onEvent(DetailEvent.ShareClicked(it)) },
                            onMoreClick = { }
                        )
                    }

                    if (state.confession.replies.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(
                                    R.string.reply_title,
                                    state.confession.replies.size
                                ),
                                style = MaterialTheme.typography.titleSmall,
                                color = ItirafTheme.colors.textPrimary,
                                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                            )
                        }
                    }

                    items(
                        items = state.confession.replies,
                        key = { it.id }
                    ) { reply ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            ReplyCard(
                                reply = reply,
                                onMoreClicked = { }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (state.confession.replies.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Henüz yorum yapılmamış.\nİlk yorumu sen yap!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ItirafTheme.colors.textTertiary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                CommentInputBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    value = state.commentText,
                    onValueChange = { onEvent(DetailEvent.CommentTextChanged(it)) },
                    onSendClick = { onEvent(DetailEvent.SendCommentClicked) },
                    isLoading = state.isSendingComment
                )
            }
        }
    }
}