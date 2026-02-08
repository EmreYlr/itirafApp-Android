package com.itirafapp.android.presentation.screens.message.inbox.inbox_detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.InboxMessage
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.message.components.InboxDetailActions
import com.itirafapp.android.presentation.screens.message.components.InboxDetailHeader
import com.itirafapp.android.presentation.screens.message.components.InboxSocialRow
import com.itirafapp.android.presentation.ui.theme.ItirafTheme
import com.itirafapp.android.util.extension.openUrlSafe

@Composable
fun InboxDetailScreen(
    data: InboxMessage,
    onApproveClick: (String, String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: InboxDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val colorParams = ItirafTheme.colors.brandPrimary.toArgb()

    LaunchedEffect(data) {
        viewModel.setInitialData(data)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is InboxDetailUiEvent.NavigateToBack -> {
                    onBackClick()
                }

                is InboxDetailUiEvent.NavigateToChat -> {
                    onApproveClick(event.roomId, event.roomName)
                }

                is InboxDetailUiEvent.NavigateToUrl -> {
                    openUrlSafe(context, event.url, colorParams)
                }

                is InboxDetailUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }

        }
    }
    InboxDetailContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxDetailContent(
    state: InboxDetailState,
    onEvent: (InboxDetailEvent) -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.sent_message_detail_title),
                canNavigateBack = true,
                onNavigateBack = { onEvent(InboxDetailEvent.BackClicked) },
                actions = {
                    InboxDetailActions(
                        onBlockUserClick = { onEvent(InboxDetailEvent.BlockUserClicked) }
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                val message = state.inboxMessage

                if (message != null) {
                    Spacer(modifier = Modifier.height(20.dp))

                    InboxDetailHeader(item = message)

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ItirafTheme.colors.backgroundCard),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_chat),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            color = ItirafTheme.colors.backgroundCard,
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp,
                                bottomEnd = 12.dp,
                                bottomStart = 4.dp
                            ),
                            modifier = Modifier.widthIn(max = 300.dp)
                        ) {
                            Text(
                                text = message.initialMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                                color = ItirafTheme.colors.textPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        color = ItirafTheme.colors.dividerColor,
                        thickness = 1.dp,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.inbox_detail_share_social),
                        style = MaterialTheme.typography.titleSmall,
                        color = ItirafTheme.colors.textSecondary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (message.requesterSocialLinks.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            message.requesterSocialLinks.forEach { link ->
                                InboxSocialRow(
                                    social = link,
                                    onClick = {
                                        onEvent(InboxDetailEvent.OpenSocialLink(link.url))
                                    }
                                )
                            }
                        }
                    } else {
                        InboxSocialRow(social = null)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                } else if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ItirafTheme.colors.brandPrimary)
                    }
                }
            }
            if (state.inboxMessage != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ItirafButton(
                        text = stringResource(R.string.reject),
                        onClick = { onEvent(InboxDetailEvent.RejectClicked) },
                        modifier = Modifier.weight(1f),
                        containerColor = ItirafTheme.colors.statusError.copy(alpha = 0.2f),
                        contentColor = ItirafTheme.colors.statusError
                    )

                    ItirafButton(
                        text = stringResource(R.string.start_chat),
                        onClick = { onEvent(InboxDetailEvent.ApproveClicked) },
                        modifier = Modifier.weight(1f),
                        containerColor = ItirafTheme.colors.brandPrimary.copy(alpha = 0.2f),
                        contentColor = ItirafTheme.colors.brandPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}