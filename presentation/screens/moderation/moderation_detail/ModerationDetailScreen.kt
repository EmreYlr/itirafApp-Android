package com.itirafapp.android.presentation.screens.moderation.moderation_detail

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.ui.theme.ItirafTheme

@Composable
fun ModerationDetailScreen(
    data: ModerationData,
    onBackClick: () -> Unit,
    viewModel: ModerationDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(data) {
        viewModel.setInitialData(data)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ModerationDetailUiEvent.NavigateBack -> {
                    onBackClick()
                }

                is ModerationDetailUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    ModerationDetailContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModerationDetailContent(
    state: ModerationDetailState,
    onEvent: (ModerationDetailEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        topBar = {
            TopBar(
                canNavigateBack = true,
                onNavigateBack = onBackClick
            )
        }
    ) { paddingValues ->

    }
}