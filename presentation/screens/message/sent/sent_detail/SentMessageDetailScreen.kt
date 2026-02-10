package com.itirafapp.android.presentation.screens.message.sent.sent_detail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.SentMessage
import com.itirafapp.android.domain.model.SentMessageStatus
import com.itirafapp.android.presentation.components.core.GenericAlertDialog
import com.itirafapp.android.presentation.components.core.ItirafButton
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.message.components.SentMessageDetailBody
import com.itirafapp.android.presentation.screens.message.components.SentMessageDetailHeader
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun SentMessageDetailScreen(
    data: SentMessage,
    onBackClick: () -> Unit,
    viewModel: SentMessageDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(data) {
        viewModel.setInitialData(data)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SentMessageDetailUiEvent.NavigateToBack -> {
                    onBackClick()
                }

                is SentMessageDetailUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
            }

        }
    }

    SentMessageDetailContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentMessageDetailContent(
    state: SentMessageDetailState,
    onEvent: (SentMessageDetailEvent) -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                title = stringResource(R.string.sent_message_detail_title),
                canNavigateBack = true,
                onNavigateBack = { onEvent(SentMessageDetailEvent.BackClicked) }
            )
        }
    ) { paddingValues ->

        if (state.showDeleteDialog) {
            GenericAlertDialog(
                onDismissRequest = { onEvent(SentMessageDetailEvent.DeleteDialogDismissed) },
                title = stringResource(R.string.sent_message_delete),
                text = stringResource(R.string.sent_message_delete_confirmation),
                confirmButtonText = stringResource(R.string.yes),
                isDestructive = true,
                onConfirmClick = { onEvent(SentMessageDetailEvent.DeleteConfirmed) },
                dismissButtonText = stringResource(R.string.cancel),
                onDismissClick = { onEvent(SentMessageDetailEvent.DeleteDialogDismissed) }
            )
        }

        val message = state.sentMessage

        if (message != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SentMessageDetailHeader(item = message)

                Spacer(modifier = Modifier.height(24.dp))

                SentMessageDetailBody(message = message)

                Spacer(modifier = Modifier.weight(1f))

                if (message.status == SentMessageStatus.PENDING) {
                    ItirafButton(
                        text = stringResource(R.string.sent_message_delete),
                        onClick = { onEvent(SentMessageDetailEvent.DeleteIconClicked) },
                        containerColor = ItirafTheme.colors.statusError.copy(alpha = 0.2f),
                        contentColor = ItirafTheme.colors.statusError
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ItirafTheme.colors.brandPrimary)
            }
        }
    }
}