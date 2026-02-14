package com.itirafapp.android.presentation.screens.my_confession.my_confession_detail

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.MyConfessionData
import com.itirafapp.android.domain.model.ReportTarget
import com.itirafapp.android.presentation.components.core.CommentInputBar
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.components.core.ReplyCard
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.mapper.toUiModel
import com.itirafapp.android.presentation.screens.my_confession.components.MyConfessionDetailHeader
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.state.ActiveDialog
import com.itirafapp.android.util.state.shareLink

@Composable
fun MyConfessionDetailScreen(
    data: MyConfessionData,
    onBackClick: () -> Unit,
    onEditClick: (MyConfessionData) -> Unit,
    onOpenReport: (ReportTarget) -> Unit,
    viewModel: MyConfessionDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(data) {
        viewModel.setInitialData(data)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MyConfessionDetailUiEvent.NavigateToBack -> {
                    onBackClick()
                }

                is MyConfessionDetailUiEvent.OpenShareSheet -> {
                    shareLink(context, event.link)
                }

                is MyConfessionDetailUiEvent.OpenReportSheet -> {
                    onOpenReport(event.target)
                }

                is MyConfessionDetailUiEvent.NavigateToEdit -> {
                    onEditClick(event.data)
                }

                is MyConfessionDetailUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    MyConfessionDetailContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyConfessionDetailContent(
    state: MyConfessionDetailState,
    onEvent: (MyConfessionDetailEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    when (val dialog = state.activeDialog) {
        is ActiveDialog.DeleteItem -> {
            GenericAlertDialog(
                onDismissRequest = { onEvent(MyConfessionDetailEvent.DismissDialog) },
                title = if (dialog.isReply) stringResource(R.string.delete_reply_title)
                else stringResource(R.string.delete_confession_title),
                text = stringResource(R.string.delete_description),
                confirmButtonText = stringResource(R.string.delete),
                onConfirmClick = { onEvent(MyConfessionDetailEvent.ConfirmAction) },
                dismissButtonText = stringResource(R.string.cancel),
                onDismissClick = { onEvent(MyConfessionDetailEvent.DismissDialog) },
                isDestructive = true
            )
        }

        is ActiveDialog.BlockUser -> {
            GenericAlertDialog(
                onDismissRequest = { onEvent(MyConfessionDetailEvent.DismissDialog) },
                title = stringResource(R.string.block_user_title),
                text = stringResource(R.string.block_user_description),
                confirmButtonText = stringResource(R.string.block),
                onConfirmClick = { onEvent(MyConfessionDetailEvent.ConfirmAction) },
                dismissButtonText = stringResource(R.string.cancel),
                onDismissClick = { onEvent(MyConfessionDetailEvent.DismissDialog) },
                isDestructive = true
            )
        }

        null -> {}
    }

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            TopBar(
                canNavigateBack = true,
                onNavigateBack = { onEvent(MyConfessionDetailEvent.BackClicked) },
                actions = {
                    state.confessions?.let { confession ->
                        IconButton(
                            onClick = {
                                onEvent(
                                    MyConfessionDetailEvent.DeleteItemClicked(
                                        confession.id,
                                        isReply = false
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = ItirafTheme.colors.statusError
                            )
                        }
                    }
                }
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
            if (state.isLoading && state.confessions == null) {
                CircularProgressIndicator(
                    color = ItirafTheme.colors.brandPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.error != null) {
                Text(
                    text = state.error.asString(),
                    color = ItirafTheme.colors.statusError,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.confessions != null) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        MyConfessionDetailHeader(
                            myConfessionDetail = state.confessions,
                            onLikeClick = { onEvent(MyConfessionDetailEvent.LikeClicked(it)) },
                            onCommentClick = {
                                focusRequester.requestFocus()
                            },
                            onShareClick = { onEvent(MyConfessionDetailEvent.ShareClicked(it)) },
                            onEditClick = { onEvent(MyConfessionDetailEvent.EditClicked) }
                        )
                    }

                    item {
                        Text(
                            text = stringResource(
                                R.string.reply_title,
                                state.confessions.reply.size
                            ),
                            style = MaterialTheme.typography.titleSmall,
                            color = ItirafTheme.colors.textPrimary,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                        )
                    }

                    items(
                        items = state.confessions.reply,
                        key = { it.id }
                    ) { reply ->
                        val isReplyMine = reply.owner.id == state.currentUserId

                        val replyUiModel = remember(reply, isReplyMine) {
                            reply.toUiModel(state.currentUserId)
                        }

                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            ReplyCard(
                                reply = replyUiModel,
                                onDeleteClick = { id ->
                                    onEvent(
                                        MyConfessionDetailEvent.DeleteItemClicked(
                                            id,
                                            isReply = true
                                        )
                                    )
                                },
                                onReportClick = { id ->
                                    onEvent(
                                        MyConfessionDetailEvent.ReportItemClicked(
                                            id,
                                            isReply = true
                                        )
                                    )
                                },
                                onBlockClick = { userId ->
                                    onEvent(
                                        MyConfessionDetailEvent.BlockUserClicked(
                                            userId,
                                            isReply = true
                                        )
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (state.confessions.reply.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Outlined.ModeComment,
                                        contentDescription = null,
                                        tint = ItirafTheme.colors.textTertiary,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.empty_noComment_title),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = ItirafTheme.colors.textTertiary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                CommentInputBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    value = state.commentText,
                    onValueChange = { onEvent(MyConfessionDetailEvent.CommentTextChanged(it)) },
                    onSendClick = { onEvent(MyConfessionDetailEvent.SendCommentClicked) },
                    isLoading = state.isLoading,
                    focusRequester = focusRequester
                )
            }
        }
    }
}