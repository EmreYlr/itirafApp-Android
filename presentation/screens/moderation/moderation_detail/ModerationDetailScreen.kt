package com.itirafapp.android.presentation.screens.moderation.moderation_detail

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itirafapp.android.R
import com.itirafapp.android.domain.model.ModerationData
import com.itirafapp.android.presentation.components.layout.TopBar
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.ApproveFormContent
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.ModerationActionSelector
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.ModerationBottomBar
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.ModerationHeaderSection
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.ModerationMetadataSection
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.ModerationPreviousStatusSection
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.ModeratorNoteField
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.RejectFormContent
import com.itirafapp.android.presentation.screens.moderation.moderation_detail.composable.StandardDivider
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
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = ItirafTheme.colors.backgroundApp,
        modifier = Modifier.imePadding(),
        topBar = {
            TopBar(
                title = stringResource(R.string.moderation_detail),
                canNavigateBack = true,
                onNavigateBack = onBackClick
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
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                ModerationHeaderSection(data = state.moderationData)

                StandardDivider()

                ModerationMetadataSection(data = state.moderationData)

                StandardDivider()

                ModerationPreviousStatusSection(rejectionReason = state.moderationData?.rejectionReason)

                ModerationActionSelector(
                    decisionMode = state.decisionMode,
                    onModeSelected = { onEvent(ModerationDetailEvent.ChangeDecisionMode(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (state.decisionMode == DecisionMode.APPROVE) {
                    ApproveFormContent(
                        isNsfw = state.isNsfw,
                        onToggleNsfw = { onEvent(ModerationDetailEvent.ToggleNsfw(it)) }
                    )
                } else {
                    RejectFormContent(
                        state = state,
                        onEvent = onEvent
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ModeratorNoteField(
                    note = state.note,
                    onNoteChange = { onEvent(ModerationDetailEvent.EnteredNote(it)) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            ModerationBottomBar(
                isLoading = state.isLoading,
                onSubmit = { onEvent(ModerationDetailEvent.Submit) }
            )
        }
    }
}